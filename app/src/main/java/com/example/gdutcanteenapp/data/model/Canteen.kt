package com.example.gdutcanteenapp.data.model

import android.view.WindowId
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "canteens")
data class Canteen(
    @PrimaryKey
    val canteenId : Int,
    val canteenName: String)
