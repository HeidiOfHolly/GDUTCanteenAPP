package com.example.gdutcanteenapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import com.example.gdutcanteenapp.data.model.FavoriteDish
import kotlinx.coroutines.flow.Flow

/*注：favorite_dishes是表名，需要在FavoriteDish文件添加@entity注解自动生成
还要在AppDatabase添加FavoriteDish::class注册这个实体*/

//删除时收藏的时候应该是在可视化页面上点击删除，然后系统内部获得点击的菜品的id，利用记录的userid调用Id来删除收藏，一般不会通过实体

@Dao
interface FavouriteDao {
    //`insert`、`delete`、`getAll`、`isFavorite`//
    // 插入收藏
    @Insert
    suspend fun insert(favoriteDish: FavoriteDish)

    // 删除收藏（通过实体）
    @Delete
    suspend fun delete(favoriteDish: FavoriteDish)

    // 删除收藏（通过用户ID和菜品ID）
    @Query("DELETE FROM favorite_dishes WHERE userId = :userId AND dishId = :dishId")
    suspend fun deleteByUserIdAndDishId(userId: String, dishId: Int)

    // 获取所有收藏（一次性）
    @Query("SELECT * FROM favorite_dishes")
    suspend fun getAll(): List<FavoriteDish>

    // 获取所有收藏（Flow 自动更新）
    @Query("SELECT * FROM favorite_dishes")
    fun getAllFlow(): Flow<List<FavoriteDish>>

    // 获取某个用户的所有收藏
    @Query("SELECT * FROM favorite_dishes WHERE userId = :userId")
    suspend fun getFavoritesByUserId(userId: String): List<FavoriteDish>

    // 获取某个用户的所有收藏（Flow）
    @Query("SELECT * FROM favorite_dishes WHERE userId = :userId")
    fun getFavoritesByUserIdFlow(userId: String): Flow<List<FavoriteDish>>

    // 检查是否已收藏
    @Query("SELECT COUNT(*) > 0 FROM favorite_dishes WHERE userId = :userId AND dishId = :dishId")
    suspend fun isFavorite(userId: String, dishId: Int): Boolean

    // 获取用户收藏的菜品ID列表
    @Query("SELECT dishId FROM favorite_dishes WHERE userId = :userId")
    suspend fun getFavoriteDishIds(userId: String): List<Int>

}