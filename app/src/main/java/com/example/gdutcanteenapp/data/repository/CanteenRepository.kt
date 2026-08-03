package com.example.gdutcanteenapp.data.repository

import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window
import kotlinx.coroutines.flow.Flow

interface CanteenRepository {

    // ========== Canteen 操作 ==========
    suspend fun getAllCanteens(): List<Canteen>
    fun getAllCanteensFlow(): Flow<List<Canteen>>
    suspend fun getCanteenById(canteenId: Int): Canteen?
    suspend fun insertCanteens(canteens: List<Canteen>)
    suspend fun insertCanteen(canteen: Canteen)
    suspend fun deleteAllCanteens()
    suspend fun deleteCanteenById(canteenId: Int)
    suspend fun deleteCanteen(canteen: Canteen)

    // ========== Window 操作 ==========
    suspend fun getWindowsByCanteen(canteenId: Int): List<Window>
    suspend fun getWindowById(windowId: Int): Window?
    suspend fun insertWindows(windows: List<Window>)
    suspend fun deleteWindowsByCanteen(canteenId: Int)
    suspend fun deleteWindowById(windowId: Int)

    // ========== Dish 操作 ==========
    suspend fun getDishesByWindow(windowId: Int): List<Dish>
    suspend fun getDishesByIds(dishIds: List<Int>): List<Dish>
    suspend fun searchDishes(keyword: String): List<Dish>
    suspend fun insertDishes(dishes: List<Dish>)
    suspend fun insertDish(dish: Dish)
    suspend fun deleteDishById(dishId: Int)
    suspend fun deleteDishesByWindow(windowId: Int)
    suspend fun deleteDishesByIds(dishIds: List<Int>)
    suspend fun deleteAllDishes()
    suspend fun getDishDetail(dishId: Int): Dish?

    /** 获取指定菜品的收藏人数，供首页推荐等场景调用 */
    suspend fun getDishFavoriteCount(dishId: Int): Int

    // ========== Favorite 操作 ==========
    // 旧接口（本地数据库兼容）
    suspend fun insertFavorite(favoriteDish: FavoriteDish)
    suspend fun deleteFavorite(userId: String, dishId: Int)
    suspend fun isFavorite(userId: String, dishId: Int): Boolean
    suspend fun getFavoriteDishIds(userId: String): List<Int>

    // 新接口（使用 API token，不再传 userId）
    suspend fun addFavoriteApi(dishId: Int): Boolean
    suspend fun removeFavoriteApi(dishId: Int): Boolean
    suspend fun getFavoriteDishesFromApi(page: Int = 1, pageSize: Int = 20): List<Dish>

    // ========== User / Auth 操作 ==========
    suspend fun insertUser(user: User)
    suspend fun login(account: String, password: String): String
    suspend fun register(account: String, username: String, password: String): String

    // ========== Tags ==========
    suspend fun getAllTagNames(): List<String>
}
