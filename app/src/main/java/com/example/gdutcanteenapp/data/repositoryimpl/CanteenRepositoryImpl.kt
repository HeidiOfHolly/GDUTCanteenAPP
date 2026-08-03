package com.example.gdutcanteenapp.data.repositoryimpl

import android.util.Log
import com.example.gdutcanteenapp.data.local.database.CanteenDao
import com.example.gdutcanteenapp.data.local.database.FavouriteDao
import com.example.gdutcanteenapp.data.local.database.UserDao
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.remote.RetrofitClient
import com.example.gdutcanteenapp.data.remote.TokenManager
import com.example.gdutcanteenapp.data.remote.dto.LoginRequest
import com.example.gdutcanteenapp.data.remote.dto.RegisterRequest
import com.example.gdutcanteenapp.data.remote.dto.toCanteen
import com.example.gdutcanteenapp.data.remote.dto.toDish
import com.example.gdutcanteenapp.data.remote.dto.toWindow
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.HttpException

class CanteenRepositoryImpl(
    private val canteenDao: CanteenDao,
    private val favouriteDao: FavouriteDao,
    private val userDao: UserDao
) : CanteenRepository {

    companion object {
        private const val TAG = "CanteenRepo"
    }

    // ========== Canteen ==========
    override suspend fun getAllCanteens(): List<Canteen> {
        try {
            val response = RetrofitClient.apiService.getAllCanteens()
            if (response.isSuccess && response.data != null) {
                val canteens = response.data.map { it.toCanteen() }
                try { canteenDao.insertCanteens(canteens) } catch (_: Exception) {}
                return canteens
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getAllCanteens failed, fallback to local", e)
        }
        return canteenDao.getAllCanteens()
    }

    override fun getAllCanteensFlow(): Flow<List<Canteen>> = canteenDao.getAllCanteensFlow()

    override suspend fun getCanteenById(canteenId: Int): Canteen? {
        try {
            val response = RetrofitClient.apiService.getCanteenDetail(canteenId)
            if (response.isSuccess && response.data != null) {
                return Canteen(response.data.canteenId, response.data.canteenName)
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getCanteenById failed, fallback to local", e)
        }
        return canteenDao.getCanteenById(canteenId)
    }

    override suspend fun insertCanteens(canteens: List<Canteen>) = canteenDao.insertCanteens(canteens)
    override suspend fun insertCanteen(canteen: Canteen) = canteenDao.insertCanteen(canteen)
    override suspend fun deleteAllCanteens() = canteenDao.deleteAllCanteens()
    override suspend fun deleteCanteenById(canteenId: Int) = canteenDao.deleteCanteenById(canteenId)
    override suspend fun deleteCanteen(canteen: Canteen) = canteenDao.deleteCanteen(canteen)

    // ========== Window ==========
    override suspend fun getWindowsByCanteen(canteenId: Int): List<Window> {
        try {
            val response = RetrofitClient.apiService.getWindowsByCanteen(canteenId)
            if (response.isSuccess && response.data != null) {
                val windows = response.data.map { it.toWindow() }
                try { canteenDao.insertWindows(windows) } catch (_: Exception) {}
                return windows
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getWindowsByCanteen failed, fallback to local", e)
        }
        return canteenDao.getWindowsByCanteen(canteenId)
    }

    override suspend fun getWindowById(windowId: Int): Window? = canteenDao.getWindowById(windowId)
    override suspend fun insertWindows(windows: List<Window>) = canteenDao.insertWindows(windows)
    override suspend fun deleteWindowsByCanteen(canteenId: Int) = canteenDao.deleteWindowsByCanteen(canteenId)
    override suspend fun deleteWindowById(windowId: Int) = canteenDao.deleteWindowById(windowId)

    // ========== Dish ==========
    override suspend fun getDishesByWindow(windowId: Int): List<Dish> {
        try {
            val response = RetrofitClient.apiService.getDishes(windowId = windowId)
            if (response.isSuccess && response.data != null) {
                val dishes = response.data.items.map { it.toDish() }
                try { canteenDao.insertDishes(dishes) } catch (_: Exception) {}
                return dishes
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getDishesByWindow failed, fallback to local", e)
        }
        return canteenDao.getDishesByWindow(windowId)
    }

    override suspend fun getDishesByIds(dishIds: List<Int>): List<Dish> {
        if (dishIds.isEmpty()) return emptyList()
        return canteenDao.getDishesByIds(dishIds)
    }

    override suspend fun searchDishes(keyword: String): List<Dish> {
        if (keyword.isBlank()) return emptyList()
        try {
            val response = RetrofitClient.apiService.getDishes(keyword = keyword)
            if (response.isSuccess && response.data != null) {
                val dishes = response.data.items.map { it.toDish() }
                try { canteenDao.insertDishes(dishes) } catch (_: Exception) {}
                return dishes
            }
        } catch (e: Exception) {
            Log.w(TAG, "API searchDishes failed, fallback to local", e)
        }
        return canteenDao.searchDishes(keyword)
    }

    override suspend fun getDishDetail(dishId: Int): Dish? {
        try {
            val response = RetrofitClient.apiService.getDishDetail(dishId)
            if (response.isSuccess && response.data != null) {
                return response.data.toDish()
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getDishDetail failed", e)
        }
        return null
    }

    override suspend fun insertDishes(dishes: List<Dish>) = canteenDao.insertDishes(dishes)
    override suspend fun insertDish(dish: Dish) = canteenDao.insertDish(dish)
    override suspend fun deleteDishById(dishId: Int) = canteenDao.deleteDishById(dishId)
    override suspend fun deleteDishesByWindow(windowId: Int) = canteenDao.deleteDishesByWindow(windowId)
    override suspend fun deleteDishesByIds(dishIds: List<Int>) {
        if (dishIds.isNotEmpty()) canteenDao.deleteDishesByIds(dishIds)
    }
    override suspend fun deleteAllDishes() = canteenDao.deleteAllDishes()

    // ========== Favorite（旧接口 — 本地数据库兼容） ==========
    override suspend fun insertFavorite(favoriteDish: FavoriteDish) {
        favouriteDao.insert(favoriteDish)
        // 同时调用 API
        if (TokenManager.isLoggedIn) {
            try { RetrofitClient.apiService.addFavorite(favoriteDish.dishId) } catch (_: Exception) {}
        }
    }

    override suspend fun deleteFavorite(userId: String, dishId: Int) {
        favouriteDao.deleteByUserIdAndDishId(userId, dishId)
        if (TokenManager.isLoggedIn) {
            try { RetrofitClient.apiService.removeFavorite(dishId) } catch (_: Exception) {}
        }
    }

    override suspend fun isFavorite(userId: String, dishId: Int): Boolean =
        favouriteDao.isFavorite(userId, dishId)

    override suspend fun getFavoriteDishIds(userId: String): List<Int> =
        favouriteDao.getFavoriteDishIds(userId)

    // ========== Favorite（新接口 — API token） ==========
    override suspend fun addFavoriteApi(dishId: Int): Boolean {
        try {
            val response = RetrofitClient.apiService.addFavorite(dishId)
            return response.isSuccess
        } catch (e: Exception) {
            Log.e(TAG, "API addFavorite failed", e)
            return false
        }
    }

    override suspend fun removeFavoriteApi(dishId: Int): Boolean {
        try {
            val response = RetrofitClient.apiService.removeFavorite(dishId)
            return response.isSuccess
        } catch (e: Exception) {
            Log.e(TAG, "API removeFavorite failed", e)
            return false
        }
    }

    override suspend fun getFavoriteDishesFromApi(page: Int, pageSize: Int): List<Dish> {
        try {
            val response = RetrofitClient.apiService.getFavorites(page, pageSize)
            if (response.isSuccess && response.data != null) {
                val dishes = response.data.items.map { it.toDish() }
                try { canteenDao.insertDishes(dishes) } catch (_: Exception) {}
                return dishes
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getFavoriteDishes failed", e)
        }
        return emptyList()
    }

    // ========== Auth ==========
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun login(account: String, password: String): String {
        val response = try {
            RetrofitClient.apiService.login(
                LoginRequest(studentNo = account, password = password)
            )
        } catch (e: Exception) {
            throw Exception("网络异常，请稍后重试")
        }
        if (response.isSuccess && response.data != null) {
            TokenManager.setToken(response.data)
            return response.data
        }
        throw Exception("学号或密码错误")
    }

    override suspend fun register(account: String, username: String, password: String): String {
        val response = try {
            RetrofitClient.apiService.register(
                RegisterRequest(studentNo = account, username = username, password = password)
            )
        } catch (e: HttpException) {
            throw toRegisterError(e)
        } catch (e: Exception) {
            throw Exception("网络异常，请稍后重试")
        }
        if (response.isSuccess && response.data != null) {
            TokenManager.setToken(response.data)
            return response.data
        }
        throw Exception(response.message.ifEmpty { "注册失败" })
    }


    private fun toRegisterError(e: HttpException): Exception {
        val errorBody = e.response()?.errorBody()?.string()
        if (errorBody != null) {
            try {
                val json = JSONObject(errorBody)
                return when (json.optInt("code")) {
                    40901 -> Exception("该学号已注册")
                    40902 -> Exception("用户名已被占用")
                    else -> Exception(json.optString("message").ifEmpty { "注册失败" })
                }
            } catch (_: Exception) {}
        }
        return Exception("注册失败")
    }

    // ========== Tags ==========
    override suspend fun getAllTagNames(): List<String> {
        try {
            val response = RetrofitClient.apiService.getAllTags()
            if (response.isSuccess && response.data != null) {
                return response.data.map { it.tagName }
            }
        } catch (e: Exception) {
            Log.w(TAG, "API getAllTags failed", e)
        }
        return emptyList()
    }
}
