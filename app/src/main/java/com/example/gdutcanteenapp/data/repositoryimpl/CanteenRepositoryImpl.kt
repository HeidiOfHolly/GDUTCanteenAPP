package com.example.gdutcanteenapp.data.repositoryimpl

import com.example.gdutcanteenapp.data.local.database.CanteenDao
import com.example.gdutcanteenapp.data.local.database.FavouriteDao
import com.example.gdutcanteenapp.data.local.database.UserDao
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.flow.Flow

//注：在viewmodel中需要手动创建
class CanteenRepositoryImpl (
    private val canteenDao: CanteenDao,
    private val favouriteDao: FavouriteDao,
    private val userDao: UserDao
) : CanteenRepository {

    // ========== Canteen 操作实现 ==========
    override suspend fun getAllCanteens(): List<Canteen> {
        return canteenDao.getAllCanteens()
    }

    override fun getAllCanteensFlow(): Flow<List<Canteen>> {
        return canteenDao.getAllCanteensFlow()
    }

    override suspend fun getCanteenById(canteenId: Int): Canteen? {
        return canteenDao.getCanteenById(canteenId)
    }

    override suspend fun insertCanteens(canteens: List<Canteen>) {
        canteenDao.insertCanteens(canteens)
    }

    override suspend fun insertCanteen(canteen: Canteen) {
        canteenDao.insertCanteen(canteen)
    }

    override suspend fun deleteAllCanteens() {
        canteenDao.deleteAllCanteens()
    }

    override suspend fun deleteCanteenById(canteenId: Int) {
        canteenDao.deleteCanteenById(canteenId)
    }

    override suspend fun deleteCanteen(canteen: Canteen) {
        canteenDao.deleteCanteen(canteen)
    }

    // ========== Window 操作实现 ==========
    override suspend fun getWindowsByCanteen(canteenId: Int): List<Window> {
        return canteenDao.getWindowsByCanteen(canteenId)
    }

    override suspend fun getWindowById(windowId: Int): Window? {
        return canteenDao.getWindowById(windowId)
    }

    override suspend fun insertWindows(windows: List<Window>) {
        canteenDao.insertWindows(windows)
    }

    override suspend fun deleteWindowsByCanteen(canteenId: Int) {
        canteenDao.deleteWindowsByCanteen(canteenId)
    }

    override suspend fun deleteWindowById(windowId: Int) {
        canteenDao.deleteWindowById(windowId)
    }

    // ========== Dish 操作实现 ==========
    override suspend fun getDishesByWindow(windowId: Int): List<Dish> {
        return canteenDao.getDishesByWindow(windowId)
    }

    override suspend fun getDishesByIds(dishIds: List<Int>): List<Dish> {
        return canteenDao.getDishesByIds(dishIds)
    }

    override suspend fun searchDishes(keyword: String): List<Dish> {
        return if (keyword.isBlank()) {
            emptyList()
        } else {
            canteenDao.searchDishes(keyword)
        }
    }

    override suspend fun insertDishes(dishes: List<Dish>) {
        canteenDao.insertDishes(dishes)
    }

    override suspend fun insertDish(dish: Dish) {
        canteenDao.insertDish(dish)
    }

    override suspend fun deleteDishById(dishId: Int) {
        canteenDao.deleteDishById(dishId)
    }

    override suspend fun deleteDishesByWindow(windowId: Int) {
        canteenDao.deleteDishesByWindow(windowId)
    }

    override suspend fun deleteDishesByIds(dishIds: List<Int>) {
        if (dishIds.isNotEmpty()) {
            canteenDao.deleteDishesByIds(dishIds)
        }
    }

    override suspend fun deleteAllDishes() {
        canteenDao.deleteAllDishes()
    }

    // ========== Favorite 操作实现 ==========
    override suspend fun insertFavorite(favoriteDish: FavoriteDish) {
        favouriteDao.insert(favoriteDish)
    }

    override suspend fun deleteFavorite(userId: String, dishId: Int) {
        favouriteDao.deleteByUserIdAndDishId(userId, dishId)
    }

    override suspend fun isFavorite(userId: String, dishId: Int): Boolean {
        return favouriteDao.isFavorite(userId, dishId)
    }

    override suspend fun getFavoriteDishIds(userId: String): List<Int> {
        return favouriteDao.getFavoriteDishIds(userId)
    }

    // ========== User 操作实现 ==========
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}