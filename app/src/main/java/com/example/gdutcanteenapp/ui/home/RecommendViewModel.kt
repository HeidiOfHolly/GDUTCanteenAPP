package com.example.gdutcanteenapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import com.example.gdutcanteenapp.data.remote.TokenManager
import com.example.gdutcanteenapp.ui.profile.FavoriteDishItem
import kotlinx.coroutines.launch

class RecommendViewModel(private val repository: CanteenRepository) : ViewModel() {

    private val _recommendDishes = MutableLiveData<List<FavoriteDishItem>>()
    val recommendDishes: LiveData<List<FavoriteDishItem>> = _recommendDishes

    private val _favoriteDishIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteDishIds: LiveData<Set<Int>> = _favoriteDishIds

    private var seeded = false

    fun loadRecommendDishes() {
        viewModelScope.launch {
            ensureSeeded()
            val dishes = repository.getTopDishesByFavoriteCount(4)
            _recommendDishes.value = dishes.map { dish ->
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

    // 切回首页时，收藏状态可能与在其他页面（收藏列表/搜索结果）的操作不同步，轻量刷新
    fun refreshFavoriteStates() {
        viewModelScope.launch {
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
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

    // 推荐卡片要展示窗口/食堂名，需要 windows 表有数据；先全量缓存食堂和窗口（只跑一次）
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
            return RecommendViewModel(repository) as T
        }
    }
}
