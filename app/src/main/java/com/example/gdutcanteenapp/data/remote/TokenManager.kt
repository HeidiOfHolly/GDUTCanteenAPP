package com.example.gdutcanteenapp.data.remote

object TokenManager {
    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    fun getToken(): String? = token

    fun clearToken() {
        token = null
    }

    val isLoggedIn: Boolean get() = token != null
}
