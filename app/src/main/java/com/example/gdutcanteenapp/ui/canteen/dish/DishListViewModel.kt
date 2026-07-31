package com.example.gdutcanteenapp.ui.canteen.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.local.mock.MockDataProvider
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.launch

class DishListViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _windowsWithDishes = MutableLiveData<List<Pair<Window, List<Dish>>>>()
    val windowsWithDishes: LiveData<List<Pair<Window, List<Dish>>>> = _windowsWithDishes

    fun load(canteenId: Int) {
        viewModelScope.launch {
            ensureSeeded()
            val windows = repository.getWindowsByCanteen(canteenId)
            val result = windows.map { window ->
                window to repository.getDishesByWindow(window.windowId)
            }
            _windowsWithDishes.value = result
        }
    }

    // 库空时用 Mock 数据灌库，保证浏览页能读到数据
    private suspend fun ensureSeeded() {
        if (repository.getAllCanteens().isNotEmpty()) return
        repository.insertCanteens(MockDataProvider.getMockCanteens())
        repository.insertWindows(MockDataProvider.getMockWindows())
        repository.insertDishes(MockDataProvider.getMockDishes())
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
