package com.example.gdutcanteenapp.data.remote.dto

import com.example.gdutcanteenapp.data.model.Dish
import com.google.gson.annotations.SerializedName

//DTO是数据传输对象的缩写
//能自动把JSON拆箱放到这个对象里
//DTO只负责网络传输，entity只负责本地存储

data class DishDto(
    @SerializedName("dishId")
    val dishId: Int,
    @SerializedName("dishName")
    val dishName: String,
    @SerializedName("dishPrice")
    val dishPrice: Double?,
    @SerializedName("dishTags")
    val dishTags: List<TagDto>,
    @SerializedName("windowId")
    val windowId: Int,
    @SerializedName("favoriteCount")
    val favoriteCount: Int,
    @SerializedName("nutrition")
    val nutrition: NutritionDto?
)

data class TagDto(
    @SerializedName("tagId")
    val tagId: Int,
    @SerializedName("tagName")
    val tagName: String,
    @SerializedName("color")
    val color: String
)

data class NutritionDto(
    @SerializedName("calories")
    val calories: Int,
    @SerializedName("protein")
    val protein: Double,
    @SerializedName("carbs")
    val carbs: Double,
    @SerializedName("fat")
    val fat: Double
)


//是一个转换器，把网络数据模型获取的数据转换成自己要使用的数据模型，比如此处是转换成Dish
fun DishDto.toDish(): Dish {
    val tagsJson = dishTags.joinToString(",") { "\"${it.tagName}\"" }
    return Dish(
        dishId = dishId,
        dishName = dishName,
        dishPrice = dishPrice?.toString() ?: "",
        dishTags = "[$tagsJson]",
        windowId = windowId
    )
}
