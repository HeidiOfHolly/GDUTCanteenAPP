package com.example.gdutcanteenapp.data.local.mock

import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.data.model.Dish

object MockDataProvider {

    // ========== 获取所有食堂 ==========
    fun getMockCanteens(): List<Canteen> {
        return listOf(
            Canteen(canteenId = 1, canteenName = "一饭（东区）"),
            Canteen(canteenId = 2, canteenName = "二饭（东区）"),
            Canteen(canteenId = 3, canteenName = "三饭（西区）"),
            Canteen(canteenId = 4, canteenName = "四饭（西区）")
        )
    }

    // ========== 获取所有窗口 ==========
    fun getMockWindows(): List<Window> {
        return listOf(
            // ===== 一饭 (canteenId = 1) =====
            Window(windowId = 1, windowName = "一楼烧腊窗口", canteenId = 1),
            Window(windowId = 2, windowName = "二楼重庆小面窗口", canteenId = 1),
            Window(windowId = 3, windowName = "三楼自选小炒窗口", canteenId = 1),

            // ===== 二饭 (canteenId = 2) =====
            Window(windowId = 4, windowName = "一楼水饺窗口", canteenId = 2),
            Window(windowId = 5, windowName = "二楼浇头面窗口", canteenId = 2),
            Window(windowId = 6, windowName = "三楼麻辣烫窗口", canteenId = 2),

            // ===== 三饭 (canteenId = 3) =====
            Window(windowId = 7, windowName = "一楼铁板饭窗口", canteenId = 3),
            Window(windowId = 8, windowName = "二楼粥粉面窗口", canteenId = 3),
            Window(windowId = 9, windowName = "三楼自选快餐窗口", canteenId = 3),

            // ===== 四饭 (canteenId = 4) =====
            Window(windowId = 10, windowName = "一楼汉堡炸鸡窗口", canteenId = 4),
            Window(windowId = 11, windowName = "二楼砂锅饭窗口", canteenId = 4),
            Window(windowId = 12, windowName = "三楼汤粉窗口", canteenId = 4)
        )
    }

    // ========== 获取所有菜品 ==========
    fun getMockDishes(): List<Dish> {
        return listOf(
            // ===== 一饭 - 烧腊窗口 (windowId = 1) =====
            Dish(
                dishId = 1,
                dishName = "黑椒鸡扒饭",
                dishPrice = "12.0",
                dishTags = "[\"热门\",\"管饱\"]",
                windowId = 1
            ),
            Dish(
                dishId = 2,
                dishName = "蜜汁叉烧饭",
                dishPrice = "15.0",
                dishTags = "[\"招牌\"]",
                windowId = 1
            ),
            Dish(
                dishId = 3,
                dishName = "烧鸭拼鸡饭",
                dishPrice = "14.0",
                dishTags = "[]",
                windowId = 1
            ),
            Dish(
                dishId = 4,
                dishName = "脆皮烧肉饭",
                dishPrice = "16.0",
                dishTags = "[\"招牌\",\"热门\"]",
                windowId = 1
            ),

            // ===== 一饭 - 重庆小面窗口 (windowId = 2) =====
            Dish(
                dishId = 5,
                dishName = "经典豌杂面",
                dishPrice = "13.0",
                dishTags = "[\"招牌\",\"辣\"]",
                windowId = 2
            ),
            Dish(
                dishId = 6,
                dishName = "红烧牛肉面",
                dishPrice = "16.0",
                dishTags = "[\"热门\",\"管饱\"]",
                windowId = 2
            ),
            Dish(
                dishId = 7,
                dishName = "酸辣粉",
                dishPrice = "11.0",
                dishTags = "[\"辣\",\"人气\"]",
                windowId = 2
            ),
            Dish(
                dishId = 8,
                dishName = "肥肠面",
                dishPrice = "17.0",
                dishTags = "[\"辣\",\"招牌\"]",
                windowId = 2
            ),

            // ===== 一饭 - 自选小炒窗口 (windowId = 3) =====
            Dish(
                dishId = 9,
                dishName = "青椒肉丝盖饭",
                dishPrice = "14.0",
                dishTags = "[\"热门\",\"管饱\"]",
                windowId = 3
            ),
            Dish(
                dishId = 10,
                dishName = "鱼香茄子盖饭",
                dishPrice = "12.0",
                dishTags = "[\"下饭\"]",
                windowId = 3
            ),
            Dish(
                dishId = 11,
                dishName = "回锅肉盖饭",
                dishPrice = "15.0",
                dishTags = "[\"招牌\",\"管饱\"]",
                windowId = 3
            ),
            Dish(
                dishId = 12,
                dishName = "麻婆豆腐盖饭",
                dishPrice = "11.0",
                dishTags = "[\"辣\",\"人气\"]",
                windowId = 3
            ),

            // ===== 二饭 - 水饺窗口 (windowId = 4) =====
            Dish(
                dishId = 13,
                dishName = "猪肉白菜水饺（12只）",
                dishPrice = "13.0",
                dishTags = "[\"招牌\",\"人气\"]",
                windowId = 4
            ),
            Dish(
                dishId = 14,
                dishName = "韭菜鸡蛋水饺（12只）",
                dishPrice = "11.0",
                dishTags = "[\"素\",\"热门\"]",
                windowId = 4
            ),
            Dish(
                dishId = 15,
                dishName = "玉米猪肉煎饺（10只）",
                dishPrice = "14.0",
                dishTags = "[]",
                windowId = 4
            ),
            Dish(
                dishId = 16,
                dishName = "三鲜水饺（12只）",
                dishPrice = "15.0",
                dishTags = "[\"招牌\"]",
                windowId = 4
            ),

            // ===== 二饭 - 浇头面窗口 (windowId = 5) =====
            Dish(
                dishId = 17,
                dishName = "雪菜肉丝面",
                dishPrice = "12.0",
                dishTags = "[\"经典\"]",
                windowId = 5
            ),
            Dish(
                dishId = 18,
                dishName = "番茄鸡蛋面",
                dishPrice = "10.0",
                dishTags = "[\"清淡\",\"人气\"]",
                windowId = 5
            ),
            Dish(
                dishId = 19,
                dishName = "香菇鸡块面",
                dishPrice = "15.0",
                dishTags = "[\"招牌\",\"管饱\"]",
                windowId = 5
            ),
            Dish(
                dishId = 20,
                dishName = "大排面",
                dishPrice = "16.0",
                dishTags = "[\"管饱\",\"热门\"]",
                windowId = 5
            ),

            // ===== 二饭 - 麻辣烫窗口 (windowId = 6) =====
            Dish(
                dishId = 21,
                dishName = "麻辣烫（自选称重）",
                dishPrice = "按称重",
                dishTags = "[\"人气\",\"辣\"]",
                windowId = 6
            ),
            Dish(
                dishId = 22,
                dishName = "麻辣香锅（单人份）",
                dishPrice = "18.0",
                dishTags = "[\"招牌\",\"辣\",\"管饱\"]",
                windowId = 6
            ),
            Dish(
                dishId = 23,
                dishName = "番茄浓汤烫菜",
                dishPrice = "15.0",
                dishTags = "[\"不辣\",\"新品\"]",
                windowId = 6
            ),
            Dish(
                dishId = 24,
                dishName = "藤椒麻辣烫",
                dishPrice = "16.0",
                dishTags = "[\"辣\",\"人气\"]",
                windowId = 6
            ),

            // ===== 三饭 - 铁板饭窗口 (windowId = 7) =====
            Dish(
                dishId = 25,
                dishName = "黑椒牛柳铁板饭",
                dishPrice = "17.0",
                dishTags = "[\"招牌\",\"热门\"]",
                windowId = 7
            ),
            Dish(
                dishId = 26,
                dishName = "照烧鸡排铁板饭",
                dishPrice = "15.0",
                dishTags = "[\"人气\"]",
                windowId = 7
            ),
            Dish(
                dishId = 27,
                dishName = "咖喱猪扒铁板饭",
                dishPrice = "16.0",
                dishTags = "[\"管饱\"]",
                windowId = 7
            ),
            Dish(
                dishId = 28,
                dishName = "香煎鸡腿肉铁板饭",
                dishPrice = "14.0",
                dishTags = "[\"新品\"]",
                windowId = 7
            ),

            // ===== 三饭 - 粥粉面窗口 (windowId = 8) =====
            Dish(
                dishId = 29,
                dishName = "皮蛋瘦肉粥 + 油条",
                dishPrice = "10.0",
                dishTags = "[\"早餐\",\"经典\"]",
                windowId = 8
            ),
            Dish(
                dishId = 30,
                dishName = "干炒牛河",
                dishPrice = "14.0",
                dishTags = "[\"人气\",\"管饱\"]",
                windowId = 8
            ),
            Dish(
                dishId = 31,
                dishName = "鲜虾云吞面",
                dishPrice = "16.0",
                dishTags = "[\"招牌\"]",
                windowId = 8
            ),
            Dish(
                dishId = 32,
                dishName = "豉汁排骨蒸肠粉",
                dishPrice = "12.0",
                dishTags = "[]",
                windowId = 8
            ),

            // ===== 三饭 - 自选快餐窗口 (windowId = 9) =====
            Dish(
                dishId = 33,
                dishName = "红烧肉套餐（两荤一素）",
                dishPrice = "15.0",
                dishTags = "[\"管饱\",\"人气\"]",
                windowId = 9
            ),
            Dish(
                dishId = 34,
                dishName = "糖醋里脊套餐（两荤一素）",
                dishPrice = "14.0",
                dishTags = "[\"热门\"]",
                windowId = 9
            ),
            Dish(
                dishId = 35,
                dishName = "番茄炒蛋套餐（一荤两素）",
                dishPrice = "11.0",
                dishTags = "[\"清淡\"]",
                windowId = 9
            ),
            Dish(
                dishId = 36,
                dishName = "红烧鱼块套餐（两荤一素）",
                dishPrice = "16.0",
                dishTags = "[\"招牌\"]",
                windowId = 9
            ),

            // ===== 四饭 - 汉堡炸鸡窗口 (windowId = 10) =====
            Dish(
                dishId = 37,
                dishName = "香辣鸡腿堡 + 薯条 + 可乐",
                dishPrice = "22.0",
                dishTags = "[\"套餐\",\"人气\"]",
                windowId = 10
            ),
            Dish(
                dishId = 38,
                dishName = "炸鸡翅（4只）",
                dishPrice = "16.0",
                dishTags = "[\"小吃\",\"热门\"]",
                windowId = 10
            ),
            Dish(
                dishId = 39,
                dishName = "牛肉汉堡 + 可乐",
                dishPrice = "20.0",
                dishTags = "[\"套餐\"]",
                windowId = 10
            ),
            Dish(
                dishId = 40,
                dishName = "鸡米花（大份）",
                dishPrice = "12.0",
                dishTags = "[\"小吃\"]",
                windowId = 10
            ),

            // ===== 四饭 - 砂锅饭窗口 (windowId = 11) =====
            Dish(
                dishId = 41,
                dishName = "腊味砂锅饭",
                dishPrice = "16.0",
                dishTags = "[\"招牌\",\"管饱\"]",
                windowId = 11
            ),
            Dish(
                dishId = 42,
                dishName = "香菇滑鸡砂锅饭",
                dishPrice = "15.0",
                dishTags = "[\"人气\"]",
                windowId = 11
            ),
            Dish(
                dishId = 43,
                dishName = "排骨砂锅饭",
                dishPrice = "17.0",
                dishTags = "[\"管饱\"]",
                windowId = 11
            ),
            Dish(
                dishId = 44,
                dishName = "牛肉砂锅饭",
                dishPrice = "18.0",
                dishTags = "[\"招牌\"]",
                windowId = 11
            ),

            // ===== 四饭 - 汤粉窗口 (windowId = 12) =====
            Dish(
                dishId = 45,
                dishName = "桂林米粉（卤肉）",
                dishPrice = "13.0",
                dishTags = "[\"经典\",\"人气\"]",
                windowId = 12
            ),
            Dish(
                dishId = 46,
                dishName = "螺蛳粉（加炸蛋）",
                dishPrice = "16.0",
                dishTags = "[\"招牌\",\"辣\",\"人气\"]",
                windowId = 12
            ),
            Dish(
                dishId = 47,
                dishName = "老友粉",
                dishPrice = "14.0",
                dishTags = "[\"辣\"]",
                windowId = 12
            ),
            Dish(
                dishId = 48,
                dishName = "猪杂汤粉",
                dishPrice = "13.0",
                dishTags = "[\"清淡\"]",
                windowId = 12
            )
        )
    }

    // ========== 获取完整数据（组合查询用） ==========
    fun getAllCanteensWithWindowsAndDishes(): Map<Canteen, List<Window>> {
        val canteens = getMockCanteens()
        val windows = getMockWindows()
        return canteens.associateWith { canteen ->
            windows.filter { it.canteenId == canteen.canteenId }
        }
    }

    fun getWindowsWithDishes(): Map<Window, List<Dish>> {
        val windows = getMockWindows()
        val dishes = getMockDishes()
        return windows.associateWith { window ->
            dishes.filter { it.windowId == window.windowId }
        }
    }
}