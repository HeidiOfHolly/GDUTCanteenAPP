package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "window")
data class Window(
    @PrimaryKey
    val windowId: Int,
    val windowName: String,
    val canteenId: Int)
