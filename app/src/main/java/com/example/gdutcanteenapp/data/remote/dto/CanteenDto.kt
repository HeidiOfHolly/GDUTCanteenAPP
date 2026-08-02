package com.example.gdutcanteenapp.data.remote.dto

import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Window
import com.google.gson.annotations.SerializedName

data class CanteenDto(
    @SerializedName("canteenId")
    val canteenId: Int,
    @SerializedName("canteenName")
    val canteenName: String
)

fun CanteenDto.toCanteen(): Canteen = Canteen(canteenId, canteenName)

data class WindowDto(
    @SerializedName("windowId")
    val windowId: Int,
    @SerializedName("windowName")
    val windowName: String,
    @SerializedName("canteenId")
    val canteenId: Int
)

fun WindowDto.toWindow(): Window = Window(windowId, windowName, canteenId)

data class CanteenDetailDto(
    @SerializedName("canteenId")
    val canteenId: Int,
    @SerializedName("canteenName")
    val canteenName: String,
    @SerializedName("windows")
    val windows: List<WindowDto>
)
