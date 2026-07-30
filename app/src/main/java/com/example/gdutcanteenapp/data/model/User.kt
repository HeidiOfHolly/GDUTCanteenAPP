package com.example.gdutcanteenapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val userId:Int,
    val userName:String,
    val userPassword: String,)
    //数据库不推荐存储列表，应该通过查询获取)
