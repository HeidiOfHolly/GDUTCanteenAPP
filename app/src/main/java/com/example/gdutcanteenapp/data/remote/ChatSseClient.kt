package com.example.gdutcanteenapp.data.remote

import com.example.gdutcanteenapp.data.remote.dto.ChatRequest
import com.example.gdutcanteenapp.data.remote.dto.ChatStreamChunk
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * SSE（Server-Sent Events）流式聊天客户端。
 *
 * ## 为什么不用 Retrofit？
 * Retrofit 的 suspend 函数适合"请求→完整响应"模式，无法原生支持 SSE 流式响应。
 * OkHttp 4.x 可以直接读取响应体字节流，逐行解析 SSE 协议，更适合流式场景。
 *
 * ## 为什么新建独立 OkHttpClient 而不是复用 RetrofitClient 的？
 * SSE 的 LLM 推理可能需要数十秒才返回第一个 token，单次连接可持续数分钟。
 * 主 RetrofitClient 的 readTimeout 只有 30 秒，会导致连接过早断开。
 * 因此这里新建一个 readTimeout=5分钟的独立客户端。
 *
 * ## 为什么用 callbackFlow？
 * OkHttp 的 response.body.source() 是阻塞式读取，callbackFlow 是 Kotlin 协程推荐的方式，
 * 用于将阻塞式回调/流式 API 桥接为 Flow，支持背压和结构化并发取消。
 */
object ChatSseClient {

    // SSE 流式端点 URL，对应接口文档 POST /api/v1/chat（响应 Content-Type: text/event-stream）
    private const val CHAT_STREAM_URL = "http://47.113.224.195:32502/api/chat"

    private val gson = Gson()

    /**
     * SSE 专用 OkHttpClient：
     * - connectTimeout 30s（建连超时）
     * - readTimeout 5min（LLM 推理可能需要较长时间才产生 token）
     * - 手动添加 Authorization header（读取 TokenManager 的 JWT token）
     */
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val token = TokenManager.getToken()
                if (token != null) {
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                } else {
                    chain.proceed(request)
                }
            }
            .build()
    }

    /**
     * 发起 SSE 流式聊天请求，返回一个 Flow<String>。
     *
     * Flow 的每个元素是 AI 回复的一个 token（通常是一个或几个字），
     * ViewModel 收集这些 token 并逐字拼接到消息气泡中。
     * 当服务器发送 [DONE] 或流正常结束时 Flow 自动关闭，
     * 当网络出错时抛出异常（由 ViewModel catch 处理）。
     *
     * @param request 包含多轮对话历史的 ChatRequest
     */
    fun streamChat(request: ChatRequest): Flow<String> = callbackFlow {
        val jsonBody = gson.toJson(request)
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        val httpRequest = Request.Builder()
            .url(CHAT_STREAM_URL)
            .header("Accept", "text/event-stream")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        // withContext(IO) 将阻塞的 OkHttp 网络调用放到后台线程
        val response = withContext(Dispatchers.IO) {
            client.newCall(httpRequest).execute()
        }

        if (!response.isSuccessful) {
            val errorBody = response.body?.string() ?: "Unknown error"
            close(IOException("SSE request failed (${response.code}): $errorBody"))
            return@callbackFlow
        }

        val source = response.body?.source() ?: run {
            close(IOException("Empty response body"))
            return@callbackFlow
        }

        try {
            // 整个 SSE 读取必须在 IO 线程执行 — OkHttp source 是阻塞网络流，
            // 留在主线程会触发 NetworkOnMainThreadException
            withContext(Dispatchers.IO) {
                while (!source.exhausted()) {
                    val line = source.readUtf8Line() ?: continue

                    if (line.startsWith("data:")) {
                        val data = line.removePrefix("data:").trim()

                        if (data == "[DONE]") return@withContext
                        if (data.isEmpty()) continue

                        val content = parseSseContent(data)
                        if (content != null) {
                            trySend(content)
                        }
                    }
                }
            }
            close()
        } catch (e: Exception) {
            close(e)  // 异常传播给 collector
        }

        // 当 Flow 被取消（如 ViewModel.onCleared 或 Fragment 销毁）时自动关闭连接
        awaitClose {
            response.close()
        }
    }

    /**
     * 解析 SSE data 行中的文本内容。
     *
     * ## 两阶段解析策略
     * 1. 先尝试 OpenAI 兼容格式：choices[0].delta.content
     * 2. 失败则 fallback：直接提取 JSON 根对象的 "content" 字段
     *
     * 这样设计的目的是兼容不同后端实现，增加健壮性。
     */
    private fun parseSseContent(data: String): String? {
        // 尝试 OpenAI 兼容格式
        try {
            val chunk = gson.fromJson(data, ChatStreamChunk::class.java)
            chunk.contentDelta?.let { return it }
        } catch (_: Exception) { /* fallback */ }

        // 回退：直接提取 "content" 字段
        try {
            val jsonObj = gson.fromJson(data, JsonObject::class.java)
            return jsonObj.get("content")?.asString
        } catch (_: Exception) {
            return null
        }
    }
}
