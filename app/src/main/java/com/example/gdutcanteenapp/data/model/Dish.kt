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
    val dishTags: String,//room不支持list所以改成string，读取Json转成字符串
    val windowId: Int)
