package com.example.gdutcanteenapp.ui.canteen.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.local.mock.MockDataProvider
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.launch

class DishListViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _windowsWithDishes = MutableLiveData<List<Pair<Window, List<Dish>>>>()
    val windowsWithDishes: LiveData<List<Pair<Window, List<Dish>>>> = _windowsWithDishes

    private val _favoriteDishIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteDishIds: LiveData<Set<Int>> = _favoriteDishIds

    fun load(canteenId: Int) {
        viewModelScope.launch {
            ensureSeeded()
            ensureDefaultUser()
            val windows = repository.getWindowsByCanteen(canteenId)
            val result = windows.map { window ->
                window to repository.getDishesByWindow(window.windowId)
            }
            _windowsWithDishes.value = result
            _favoriteDishIds.value = repository.getFavoriteDishIds(CURRENT_USER_ID).toSet()
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

    fun refreshFavorites() {
        viewModelScope.launch {
            _favoriteDishIds.value = repository.getFavoriteDishIds(CURRENT_USER_ID).toSet()
        }
    }

    // 窗口表空时用 Mock 数据灌库，保证浏览页能读到数据
    // 不能用 getAllCanteens 判断：列表页已单独灌过 canteens 表
    private suspend fun ensureSeeded() {
        if (repository.getWindowsByCanteen(1).isNotEmpty()) return
        repository.insertCanteens(MockDataProvider.getMockCanteens())
        repository.insertWindows(MockDataProvider.getMockWindows())
        repository.insertDishes(MockDataProvider.getMockDishes())
    }

    private suspend fun ensureDefaultUser() {
        repository.insertUser(User(userId = CURRENT_USER_ID, userName = "默认用户", userAccount = "user001", userPassword = "123456"))
    }

    class Factory(
        private val repository: CanteenRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DishListViewModel(repository) as T
        }
    }

    companion object {
        private const val CURRENT_USER_ID = "1"
    }
}
