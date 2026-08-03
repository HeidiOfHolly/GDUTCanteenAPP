package com.example.gdutcanteenapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("studentNo")
    val studentNo: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("studentNo")
    val studentNo: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

data class FavoriteResultDto(
    @SerializedName("dishId")
    val dishId: Int,
    @SerializedName("favorite")
    val favorite: Boolean,
    @SerializedName("favoriteCount")
    val favoriteCount: Int
)
