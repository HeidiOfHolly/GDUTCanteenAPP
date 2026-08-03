package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// 只保留 User 外键：搜索结果里的 API 菜品往往不在本地 dishes 表中，
// 若再对 Dish 建外键，收藏这类菜品会直接触发 FOREIGN KEY constraint failed
@Entity(
    tableName = "favorite_dishes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // 用户删除 → 该用户的收藏自动删除
        )
    ],
    indices = [
        Index("userId"),      // 方便按用户查询
        Index("dishId"),      // 方便按菜品查询
        Index(value = ["userId", "dishId"], unique = true)  // 同一用户不能重复收藏同一菜品
    ]
)
data class FavoriteDish(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,          // 自增主键
    val userId: String,          // 用户ID（外键）
    val dishId: Int,          // 菜品ID（外键）
)
