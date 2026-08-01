package com.example.gdutcanteenapp.ui.canteen.FavoriteDish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.local.mock.MockDataProvider
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: CanteenRepository) : ViewModel() {

    private val _items = MutableLiveData<List<FavoriteDishItem>>()
    val items: LiveData<List<FavoriteDishItem>> = _items

    // 按关键词搜索菜品（按菜名模糊匹配）
    fun searchDishes(keyword: String) {
        viewModelScope.launch {
            ensureSeeded()
            _items.value = toItems(repository.searchDishes(keyword))
        }
    }

    // 加载用户收藏的菜品
    fun loadDishes() {
        viewModelScope.launch {
            ensureSeeded()
            ensureDefaultUser()
            val favoriteDishIds = repository.getFavoriteDishIds(CURRENT_USER_ID)
            // 空列表直接跳过，否则 IN () 会 SQL 报错
            val dishes = if (favoriteDishIds.isEmpty()) {
                emptyList()
            } else {
                repository.getDishesByIds(favoriteDishIds)
            }
            _items.value = toItems(dishes)
        }
    }

    // 每道菜补上窗口名和食堂名，组装成一行数据（Dish 本身不含这两项）
    private suspend fun toItems(dishes: List<Dish>): List<FavoriteDishItem> {
        return dishes.map { dish ->
            val window = repository.getWindowById(dish.windowId)
            val canteen = window?.let { repository.getCanteenById(it.canteenId) }
            FavoriteDishItem(
                dish = dish,
                canteenName = canteen?.canteenName ?: "",
                windowName = window?.windowName ?: ""
            )
        }
    }

    // 窗口表空时用 Mock 数据灌库，保证能查到菜品
    private suspend fun ensureSeeded() {
        if (repository.getWindowsByCanteen(1).isNotEmpty()) return
        repository.insertCanteens(MockDataProvider.getMockCanteens())
        repository.insertWindows(MockDataProvider.getMockWindows())
        repository.insertDishes(MockDataProvider.getMockDishes())
    }

    private suspend fun ensureDefaultUser() {
        repository.insertUser(User(userId = CURRENT_USER_ID, userName = "默认用户", userPassword = "123456"))
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
        private const val CURRENT_USER_ID = 1
    }
}
