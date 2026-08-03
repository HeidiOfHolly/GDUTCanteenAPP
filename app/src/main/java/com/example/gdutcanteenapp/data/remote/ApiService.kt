package com.example.gdutcanteenapp.data.remote

import com.example.gdutcanteenapp.data.remote.dto.CanteenDetailDto
import com.example.gdutcanteenapp.data.remote.dto.CanteenDto
import com.example.gdutcanteenapp.data.remote.dto.DishDto
import com.example.gdutcanteenapp.data.remote.dto.FavoriteResultDto
import com.example.gdutcanteenapp.data.remote.dto.LoginRequest
import com.example.gdutcanteenapp.data.remote.dto.RegisterRequest
import com.example.gdutcanteenapp.data.remote.dto.TagDto
import com.example.gdutcanteenapp.data.remote.dto.WindowDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // ========== 食堂与窗口 ==========

    @GET("canteens")
    suspend fun getAllCanteens(
        @Query("keyword") keyword: String? = null
    ): ApiResponse<List<CanteenDto>>

    @GET("canteens/{canteenId}")
    suspend fun getCanteenDetail(
        @Path("canteenId") canteenId: Int
    ): ApiResponse<CanteenDetailDto>

    @GET("canteens/{canteenId}/windows")
    suspend fun getWindowsByCanteen(
        @Path("canteenId") canteenId: Int
    ): ApiResponse<List<WindowDto>>

    // ========== 菜品 ==========

    @GET("dishes")
    suspend fun getDishes(
        @Query("keyword") keyword: String? = null,
        @Query("canteenId") canteenId: Int? = null,
        @Query("windowId") windowId: Int? = null,
        @Query("tagIds") tagIds: String? = null,
        @Query("tagMode") tagMode: String? = null,
        @Query("maxPrice") maxPrice: String? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): ApiResponse<PageResult<DishDto>>

    @GET("dishes/{dishId}")
    suspend fun getDishDetail(
        @Path("dishId") dishId: Int
    ): ApiResponse<DishDto>

    // ========== 标签 ==========

    @GET("tags")
    suspend fun getAllTags(): ApiResponse<List<TagDto>>

    // ========== 用户认证 ==========

    @POST("users/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<String>

    @POST("users/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<String>

    // ========== 用户收藏（需要 Authorization header） ==========

    @GET("users/me/favorites")
    suspend fun getFavorites(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): ApiResponse<PageResult<DishDto>>

    @PUT("users/me/favorites/{dishId}")
    suspend fun addFavorite(
        @Path("dishId") dishId: Int
    ): ApiResponse<FavoriteResultDto>

    @DELETE("users/me/favorites/{dishId}")
    suspend fun removeFavorite(
        @Path("dishId") dishId: Int
    ): ApiResponse<FavoriteResultDto>

    // ========== 推荐 ==========

    @GET("recommendations/today")
    suspend fun getTodayRecommendations(
        @Query("limit") limit: Int = 5
    ): ApiResponse<List<DishDto>>

    @GET("recommendations/random")
    suspend fun getRandomRecommendation(
        @Query("canteenId") canteenId: Int? = null,
        @Query("tagIds") tagIds: String? = null,
        @Query("maxPrice") maxPrice: String? = null
    ): ApiResponse<DishDto>
}
