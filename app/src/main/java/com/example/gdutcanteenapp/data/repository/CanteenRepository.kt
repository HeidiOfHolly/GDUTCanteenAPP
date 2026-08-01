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

    // ========== Favorite 操作 ==========
    suspend fun insertFavorite(favoriteDish: FavoriteDish)
    suspend fun deleteFavorite(userId: Int, dishId: Int)
    suspend fun isFavorite(userId: Int, dishId: Int): Boolean
    suspend fun getFavoriteDishIds(userId: Int): List<Int>

    // ========== User 操作 ==========
    suspend fun insertUser(user: User)
}