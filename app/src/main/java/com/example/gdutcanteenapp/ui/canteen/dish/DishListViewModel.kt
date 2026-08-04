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

class DishListViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _windowsWithDishes = MutableLiveData<List<Pair<Window, List<Dish>>>>()
    val windowsWithDishes: LiveData<List<Pair<Window, List<Dish>>>> = _windowsWithDishes

    private val _favoriteDishIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteDishIds: LiveData<Set<Int>> = _favoriteDishIds

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
            val current = _favoriteDishIds.value ?: emptySet()
            if (dishId in current) {
                repository.deleteFavorite(TokenManager.getUserId(), dishId)
            } else {
                repository.insertFavorite(FavoriteDish(userId = TokenManager.getUserId(), dishId = dishId))
            }
            _favoriteDishIds.value = repository.getFavoriteDishIds(TokenManager.getUserId()).toSet()
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
