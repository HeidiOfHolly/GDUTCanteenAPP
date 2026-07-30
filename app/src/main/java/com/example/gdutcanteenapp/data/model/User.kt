package com.example.gdutcanteenapp.data.model

data class User(val userId:Int,
                val userName:String,
                val userPassword: String,
                 val FavoriteDishes: List<Int>)
