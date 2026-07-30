package com.example.gdutcanteenapp.data.model

data class Dish(val dishId: Int,
                val dishName: String,
                val dishPrice: String,
                val dishTags: List<String>,
                val windowId: Int)
