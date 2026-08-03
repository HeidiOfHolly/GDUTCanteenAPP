package com.example.gdutcanteenapp.data.remote.dto

import com.example.gdutcanteenapp.data.model.Dish
import com.google.gson.annotations.SerializedName

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
