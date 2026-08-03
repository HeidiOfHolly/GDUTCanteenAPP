package com.example.gdutcanteenapp.data.remote


//后端的返回格式是这样，真正需要的数据存在data里
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
) {
    val isSuccess: Boolean get() = code == 0
}


//返回分页数据时用这个包装
data class PageResult<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Long,
    val hasMore: Boolean
)
