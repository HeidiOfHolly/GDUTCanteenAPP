package com.example.gdutcanteenapp.data.local.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.Window
import kotlinx.coroutines.flow.Flow

//注：canteens是表名，需要在canteen中添加注解生成

interface CanteenDao {
    // ========== Canteen 操作 ==========
    @Query("SELECT * FROM canteens ORDER BY id")
    suspend fun getAllCanteens(): List<Canteen>//获取所有餐厅（仅一次）

    @Query("SELECT * FROM canteens ORDER BY id")
    fun getAllCanteensFlow(): Flow<List<Canteen>>//自动更新获取餐厅

    @Query("SELECT * FROM canteens WHERE id = :canteenId")
    suspend fun getCanteenById(canteenId: Int): Canteen?//根据id获取餐厅

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCanteens(canteens: List<Canteen>)//添加多个餐厅

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCanteen(canteen: Canteen)//添加单个餐厅

    @Query("DELETE FROM canteens")
    suspend fun deleteAllCanteens()//删除所有餐厅

    @Query("DELETE FROM canteens WHERE id = :canteenId")
    suspend fun deleteCanteenById(canteenId: Int)  // 按ID删除单个

    @Delete
    suspend fun deleteCanteen(canteen: Canteen)  // 通过实体删除

    // ========== Window 操作 ==========
    @Query("SELECT * FROM windows WHERE canteenId = :canteenId ORDER BY id")
    suspend fun getWindowsByCanteen(canteenId: Int): List<Window>//根据餐厅Id获取窗口

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWindows(windows: List<Window>)//添加多个窗口

    @Query("DELETE FROM windows WHERE canteenId = :canteenId")
    suspend fun deleteWindowsByCanteen(canteenId: Int)//根据餐厅id删除窗口

    @Query("DELETE FROM windows WHERE id = :windowId")
    suspend fun deleteWindowById(windowId: Int)//删除单个窗口

    // ========== Dish 操作 ==========
    @Query("SELECT * FROM dishes WHERE canteenId = :canteenId ORDER BY id")
    suspend fun getDishesByCanteen(canteenId: Int): List<Dish>//根据餐厅获取菜品

    @Query("SELECT * FROM dishes WHERE windowId = :windowId ORDER BY id")
    suspend fun getDishesByWindow(windowId: Int): List<Dish>//根据窗口获取菜品

    @Query("SELECT * FROM dishes WHERE name LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%'")
    suspend fun searchDishes(keyword: String): List<Dish>//根据关键字获取菜品

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<Dish>)//添加多个菜品

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)//添加单个个菜品

    //1.根据食堂ID删除所有菜品
    @Query("DELETE FROM dishes WHERE canteenId = :canteenId")
    suspend fun deleteDishesByCanteen(canteenId: Int)

    // 2. 删除单个菜品（通过ID）
    @Query("DELETE FROM dishes WHERE id = :dishId")
    suspend fun deleteDishById(dishId: Int)

    // 3. 根据窗口ID删除所有菜品
    @Query("DELETE FROM dishes WHERE windowId = :windowId")
    suspend fun deleteDishesByWindow(windowId: Int)

    // 4. 删除多个菜品（通过ID列表）
    @Query("DELETE FROM dishes WHERE id IN (:dishIds)")
    suspend fun deleteDishesByIds(dishIds: List<Int>)

    // 5. 删除所有菜品
    @Query("DELETE FROM dishes")
    suspend fun deleteAllDishes()
}