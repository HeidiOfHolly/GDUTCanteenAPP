package com.example.gdutcanteenapp.ui.canteen.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import com.example.gdutcanteenapp.data.remote.TokenManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DishListViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _windowsWithDishes = MutableLiveData<List<Pair<Window, List<Dish>>>>()
    val windowsWithDishes: LiveData<List<Pair<Window, List<Dish>>>> = _windowsWithDishes

    private val _favoriteDishIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteDishIds: LiveData<Set<Int>> = _favoriteDishIds

    // 串行化收藏切换，避免连点同一菜品时并发读到旧状态重复插入，触发唯一索引冲突闪退
    private val toggleMutex = Mutex()

    fun load(canteenId: Int) {
        viewModelScope.launch {
            ensureDefaultUser()
            val windows = repository.getWindowsByCanteen(canteenId)
            val result = windows.map { window ->
                window to repository.getDishesByWindow(window.windowId)
            }
            _windowsWithDishes.value = result
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
        }
    }

    fun toggleFavorite(dishId: Int) {
        viewModelScope.launch {
            toggleMutex.withLock {
                val current = _favoriteDishIds.value ?: emptySet()
                //判断菜品是不是在收藏里面·
                if (dishId in current) {
                    repository.deleteFavorite(TokenManager.getUserId(), dishId)
                } else {
                    repository.insertFavorite(FavoriteDish(userId = TokenManager.getUserId(), dishId = dishId))
                }
                _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
            }
        }
    }

    fun refreshFavorites() {
        viewModelScope.launch {
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
        }
    }



    private suspend fun ensureDefaultUser() {
        repository.insertUser(User(userId = TokenManager.getUserId(), userName = TokenManager.getUserName(), userAccount = TokenManager.getUserId(), userPassword = ""))
    }

    class Factory(
        private val repository: CanteenRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DishListViewModel(repository) as T
        }
    }
}
