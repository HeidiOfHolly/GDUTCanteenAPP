package com.example.gdutcanteenapp.data.remote

import android.util.Base64
import android.util.Log
import org.json.JSONObject

object TokenManager {
    private const val TAG = "TokenManager"

    private var token: String? = null
    private var userId: String? = null
    private var userName: String? = null

    fun setToken(token: String?, loginAccount: String? = null, loginName: String? = null) {
        this.token = token
        if (token != null) {
            parseJwtPayload(token)
        } else {
            userId = null
            userName = null
        }
        // 如果 JWT 解析没拿到 userId，用登录时填的学号兜底
        if (userId == null && loginAccount != null) {
            userId = loginAccount
            Log.d(TAG, "JWT 解析失败，降级使用登录学号: $loginAccount")
        }
        if (userName == null && loginName != null) {
            userName = loginName
        }
        Log.d(TAG, "setToken: userId=$userId, userName=$userName")
    }

    fun getToken(): String? = token
    fun getUserId(): String = userId ?: "0"
    fun getUserName(): String = userName ?: "未登录"

    fun clearToken() {
        token = null
        userId = null
        userName = null
    }

    val isLoggedIn: Boolean get() = token != null

    private fun parseJwtPayload(token: String) {
        try {
            val parts = token.split(".")
            Log.d(TAG, "token 分段数: ${parts.size}")
            if (parts.size < 2) {
                Log.w(TAG, "token 不是 JWT 格式（缺少 '.' 分隔符），无法解析 payload")
                return
            }
            val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE))
            Log.d(TAG, "JWT payload: $payloadJson")
            val json = JSONObject(payloadJson)
            userId = json.optString("studentNo", null)
            userName = json.optString("username", null)
            Log.d(TAG, "JWT 解析: studentNo=$userId, username=$userName")
        } catch (e: Exception) {
            Log.e(TAG, "JWT 解析异常", e)
        }
    }
}
