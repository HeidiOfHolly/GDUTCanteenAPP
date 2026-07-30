package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "canteen")
data class Canteen(
    @PrimaryKey
    val canteenId : Int,
    val canteenName: String)
