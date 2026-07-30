package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "windows",
    foreignKeys = [
        ForeignKey(
            entity = Canteen::class,
            parentColumns = ["canteenId"],
            childColumns = ["canteenId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Window(
    @PrimaryKey
    val windowId: Int,
    val windowName: String,
    val canteenId: Int
)
