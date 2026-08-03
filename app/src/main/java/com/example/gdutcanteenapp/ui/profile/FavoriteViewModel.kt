package com.example.gdutcanteenapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.local.mock.MockDataProvider
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.repository.CanteenRepository
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

    fun searchDishes(keyword: String) {
        viewModelScope.launch {
            ensureSeeded()
            ensureDefaultUser()
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
            _favoriteDishIds.value = repository.getFavoriteDishIds(CURRENT_USER_ID).toSet()
        }
    }

    fun load() {
        viewModelScope.launch {
            ensureSeeded()
            ensureDefaultUser()
            val dishIds = repository.getFavoriteDishIds(CURRENT_USER_ID)
            _favoriteDishIds.value = dishIds.toSet()
            if (dishIds.isEmpty()) {
                _favoriteItems.value = emptyList()
                return@launch
            }
            var dishes = repository.getDishesByIds(dishIds)
            val cachedIds = dishes.map { it.dishId }.toSet()
            val missingIds = dishIds.filterNot { it in cachedIds }
            // 本地没缓存到的收藏菜品（写入时外键约束被静默吞掉等），从 API 补拉，保证收藏列表有内容
            if (missingIds.isNotEmpty()) {
                val fetched = missingIds.mapNotNull { repository.getDishDetail(it) }
                if (fetched.isNotEmpty()) {
                    repository.insertDishes(fetched)
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
        }
    }

    fun toggleFavorite(dishId: Int) {
        viewModelScope.launch {
            val current = _favoriteDishIds.value ?: emptySet()
            if (dishId in current) {
                repository.deleteFavorite(CURRENT_USER_ID, dishId)
            } else {
                repository.insertFavorite(FavoriteDish(userId = CURRENT_USER_ID, dishId = dishId))
            }
            _favoriteDishIds.value = repository.getFavoriteDishIds(CURRENT_USER_ID).toSet()
        }
    }

    fun removeFavorite(dishId: Int) {
        viewModelScope.launch {
            repository.deleteFavorite(CURRENT_USER_ID, dishId)
            load()
        }
    }

    private suspend fun ensureSeeded() {
        if (seeded) return
        val canteens = repository.getAllCanteens()
        if (canteens.isEmpty()) {
            // API 不可用：灌入本地 mock 数据
            repository.insertCanteens(MockDataProvider.getMockCanteens())
            repository.insertWindows(MockDataProvider.getMockWindows())
            repository.insertDishes(MockDataProvider.getMockDishes())
        } else {
            // API 可用：把每个食堂的窗口缓存到本地，搜索结果的菜品才能解析出窗口名和食堂名
            canteens.forEach { canteen ->
                repository.getWindowsByCanteen(canteen.canteenId)
            }
        }
        seeded = true
    }

    private suspend fun ensureDefaultUser() {
        repository.insertUser(User(userId = CURRENT_USER_ID, userName = "默认用户", userAccount = "user001", userPassword = "123456"))
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
        private const val CURRENT_USER_ID = "1"
    }
}
