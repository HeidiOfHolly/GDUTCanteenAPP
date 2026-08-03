package com.example.gdutcanteenapp.data.remote

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
) {
    val isSuccess: Boolean get() = code == 0
}

data class PageResult<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Long,
    val hasMore: Boolean
)
