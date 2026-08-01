package com.example.gdutcanteenapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    fun load() {
        viewModelScope.launch {
            val dishIds = repository.getFavoriteDishIds(CURRENT_USER_ID)
            if (dishIds.isEmpty()) {
                _favoriteItems.value = emptyList()
                return@launch
            }
            val dishes = repository.getDishesByIds(dishIds)
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

    fun removeFavorite(dishId: Int) {
        viewModelScope.launch {
            repository.deleteFavorite(CURRENT_USER_ID, dishId)
            load()
        }
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
