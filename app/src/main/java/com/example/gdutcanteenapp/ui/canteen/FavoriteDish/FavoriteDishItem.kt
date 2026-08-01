package com.example.gdutcanteenapp.ui.canteen.FavoriteDish

import com.example.gdutcanteenapp.data.model.Dish

// 收藏列表的一行：菜品 + 它所属的窗口名和食堂名（Dish 本身不含这两项）
data class FavoriteDishItem(
    val dish: Dish,
    val canteenName: String,
    val windowName: String
)
