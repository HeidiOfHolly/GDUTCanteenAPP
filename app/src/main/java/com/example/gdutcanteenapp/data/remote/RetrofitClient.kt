package com.example.gdutcanteenapp.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://47.113.224.195:30080/api/v1/"


    //认证拦截器，每次请求前自动检测是否有token，如果有就加入请求头
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val token = TokenManager.getToken()
        val newRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        val response = chain.proceed(newRequest)
        if (response.code == 401) TokenManager.clearToken()
        response
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)//设置服务器地址
            .client(okHttpClient)//设置网络客户端
            .addConverterFactory(GsonConverterFactory.create())//将JSON自动转换为kotlin
            .build()
    }

    //对外暴露API实例，可以供其他代码调用
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
