package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish")
data class Dish(
    @PrimaryKey
    val dishId: Int,
    val dishName: String,
    val dishPrice: String,
    val dishTags: List<String>,
    val windowId: Int)
