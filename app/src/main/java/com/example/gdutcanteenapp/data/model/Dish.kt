package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "dishes",
        foreignKeys = [ForeignKey(
    entity = Window::class,
    parentColumns = ["windowId"],
    childColumns = ["windowId"],
    onDelete = ForeignKey.CASCADE
)])
data class Dish(
    @PrimaryKey
    val dishId: Int,
    val dishName: String,
    val dishPrice: String,
    val dishTags: String,
    val windowId: Int,
    /** 收藏人数，从 API 获取，默认 0 */
    val favoriteCount: Int = 0
)

