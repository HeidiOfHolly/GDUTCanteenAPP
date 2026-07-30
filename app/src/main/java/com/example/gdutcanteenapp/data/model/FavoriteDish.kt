package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_dishes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // 用户删除 → 该用户的收藏自动删除
        ),
        ForeignKey(
            entity = Dish::class,
            parentColumns = ["dishId"],
            childColumns = ["dishId"],
            onDelete = ForeignKey.CASCADE  // 菜品删除 → 对应收藏自动删除
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
    val userId: Int,          // 用户ID（外键）
    val dishId: Int,          // 菜品ID（外键）
)
