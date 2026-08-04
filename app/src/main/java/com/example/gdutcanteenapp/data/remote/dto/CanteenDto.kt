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

//也是转换成自己APP里需要的数据类型，保证完全一样，可以进行存入本地或者运行等方法
fun WindowDto.toWindow(): Window = Window(windowId, windowName, canteenId)

data class CanteenDetailDto(
    @SerializedName("canteenId")
    val canteenId: Int,
    @SerializedName("canteenName")
    val canteenName: String,
    @SerializedName("windows")
    val windows: List<WindowDto>
)
