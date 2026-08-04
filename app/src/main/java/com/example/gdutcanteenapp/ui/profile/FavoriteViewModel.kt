package com.example.gdutcanteenapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import com.example.gdutcanteenapp.data.remote.TokenManager
import kotlinx.coroutines.launch

data class FavoriteDishItem(
    val dishId: Int,
    val dishName: String,
    val dishPrice: String,
    val dishTags: String,
    val windowName: String,
    val canteenName: String
)

class FavoriteViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _favoriteItems = MutableLiveData<List<FavoriteDishItem>>()
    val favoriteItems: LiveData<List<FavoriteDishItem>> = _favoriteItems

    private val _favoriteDishIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteDishIds: LiveData<Set<Int>> = _favoriteDishIds

    private var seeded = false

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchDishes(keyword: String) {
        viewModelScope.launch {
            // 先缓存窗口/食堂，否则搜索结果里的窗口名、食堂名会因本地无数据而空白
            ensureSeeded()
            val dishes = repository.searchDishes(keyword)
            _favoriteItems.value = dishes.map { dish ->
                val window = repository.getWindowById(dish.windowId)
                val canteen = window?.let { repository.getCanteenById(it.canteenId) }
                FavoriteDishItem(
                    dishId = dish.dishId,
                    dishName = dish.dishName,
                    dishPrice = dish.dishPrice,
                    dishTags = formatTags(dish.dishTags),
                    windowName = window?.windowName ?: "",
                    canteenName = canteen?.canteenName ?: ""
                )
            }
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
        }
    }

    fun load() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val dishIds = repository.getFavoriteDishIds(TokenManager.getUserId())
                _favoriteDishIds.value = dishIds.toSet()
                if (dishIds.isEmpty()) {
                    _favoriteItems.value = emptyList()
                    return@launch
                }
                // 先确保窗口/食堂缓存到本地：insertDishes 对窗口有外键约束，窗口缺失会抛
                // FOREIGN KEY constraint failed 导致闪退；补上后也能展示窗口名和食堂名
                ensureSeeded()
                var dishes = repository.getDishesByIds(dishIds)
                val cachedIds = dishes.map { it.dishId }.toSet()
                val missingIds = dishIds.filterNot { it in cachedIds }
                // 本地没缓存到的收藏菜品（写入时外键约束被静默吞掉等），从 API 补拉，保证收藏列表有内容
                if (missingIds.isNotEmpty()) {
                    val fetched = missingIds.mapNotNull { repository.getDishDetail(it) }
                    if (fetched.isNotEmpty()) {
                        try {
                            repository.insertDishes(fetched)
                        } catch (e: Exception) {
                            Log.w(TAG, "insertDishes failed, 仅内存展示", e)
                        }
                        dishes = dishes + fetched
                    }
                }
                val items = dishes.map { dish ->
                    val window = repository.getWindowById(dish.windowId)
                    val canteen = window?.let { repository.getCanteenById(it.canteenId) }
                    FavoriteDishItem(
                        dishId = dish.dishId,
                        dishName = dish.dishName,
                        dishPrice = dish.dishPrice,
                        dishTags = formatTags(dish.dishTags),
                        windowName = window?.windowName ?: "",
                        canteenName = canteen?.canteenName ?: ""
                    )
                }
                _favoriteItems.value = items
            } catch (e: Exception) {
                Log.w(TAG, "load favorites failed", e)
            }
            finally {
                _isLoading.value = false}
        }
    }

    fun toggleFavorite(dishId: Int) {
        viewModelScope.launch {
            val current = _favoriteDishIds.value ?: emptySet()
            if (dishId in current) {
                repository.deleteFavorite(TokenManager.getUserId(), dishId)
            } else {
                repository.insertFavorite(FavoriteDish(userId = TokenManager.getUserId(), dishId = dishId))
            }
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
        }
    }

    fun removeFavorite(dishId: Int) {
        viewModelScope.launch {
            repository.deleteFavorite(TokenManager.getUserId(), dishId)
            load()
        }
    }

    private suspend fun ensureSeeded() {
        if (seeded) return
        val canteens = repository.getAllCanteens()
        if (canteens.isEmpty()) return
        canteens.forEach { canteen ->
            repository.getWindowsByCanteen(canteen.canteenId)

        }
        seeded = true
    }


    private fun formatTags(raw: String): String {
        return raw.trim()
            .removePrefix("[").removeSuffix("]")
            .split(",")
            .map { it.trim().trim('"') }
            .filter { it.isNotEmpty() }
            .joinToString("，")
    }

    class Factory(
        private val repository: CanteenRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repository) as T
        }
    }

    companion object {
        private const val TAG = "FavoriteVM"
    }
}
