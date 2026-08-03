package com.example.gdutcanteenapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.model.Msg
import com.example.gdutcanteenapp.data.remote.ChatSseClient
import com.example.gdutcanteenapp.data.remote.RetrofitClient
import com.example.gdutcanteenapp.data.remote.dto.ChatRequest
import com.example.gdutcanteenapp.data.remote.dto.ChatSessionRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.util.Log
import java.util.UUID

/**
 * 聊天页面的 ViewModel。
 *
 * ## 职责
 * - 管理消息列表（用户消息 + AI 回复 + 思考中占位）
 * - 管理会话 ID：首次发消息时创建会话，后续消息复用同一个 sessionId
 * - 通过 ChatSseClient 发起 SSE 流式请求，收集 token 并逐字更新 AI 消息气泡
 * - 管理 isSending 状态防止重复发送
 *
 * ## 设计思路
 * ### 为什么需要 sessionId？
 * 接口文档中，POST /api/v1/chat 的 sessionId 是可选的（为空时后端自动创建新会话）。
 * 但 SSE 响应格式未明确标注 sessionId 传递方式，因此采用更可靠的做法：
 * 首次发消息前先调用 POST /api/v1/chat/sessions 创建会话拿到 sessionId，
 * 后续所有消息都带上 sessionId，确保多轮对话绑定在同一个会话中。
 *
 * ### 为什么用 UUID 作为 clientMessageId？
 * 接口要求每条消息附带唯一 ID，用于去重和幂等。UUID v4 是标准做法，无需服务端生成。
 *
 * ### 流式更新策略
 * ChatSseClient 返回 Flow<String>，每个 token 抵达时：
 * 1. 首个 token → 用空内容 AI 消息替换「正在思考…」占位
 * 2. 每个 token → 追加到 AI 消息 content，通过 LiveData 推送整个列表到 UI
 * 3. 流结束 → Flow 自动 close，isSending = false
 * 4. 异常 → 移除思考占位，追加错误提示消息
 */
class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Msg>>(emptyList())
    val messages: LiveData<List<Msg>> = _messages

    private val _isSending = MutableLiveData(false)
    val isSending: LiveData<Boolean> = _isSending

    /** 当前会话 ID，首次发消息时通过 API 创建 */
    private var sessionId: Long? = null

    /** 当前 SSE 流式请求的协程 Job，用于取消 */
    private var streamJob: Job? = null

    /**
     * 发送用户消息并接收 AI 流式回复。
     *
     * 流程：
     * 1. 防抖检查（空消息 / 正在发送中）
     * 2. 添加用户消息气泡（TYPE_SENT，右对齐蓝色）
     * 3. 添加「正在思考…」占位气泡（TYPE_THINKING，左对齐灰色）
     * 4. 如果没有 sessionId → 调用 API 创建会话
     * 5. 发起 SSE 流式请求
     * 6. 首个 token → 用空 AI 气泡替换占位气泡
     * 7. 每个 token → 追加到 AI 气泡内容尾部
     * 8. 异常 → 错误提示
     */
    fun sendMessage(content: String) {
        if (content.isBlank() || _isSending.value == true) return

        val currentList = _messages.value.orEmpty().toMutableList()

        // 1. 添加用户消息
        currentList.add(Msg(content = content.trim(), type = Msg.TYPE_SENT))

        // 2. 添加思考中占位
        currentList.add(Msg(content = "", type = Msg.TYPE_THINKING))
        _messages.value = currentList.toList()
        _isSending.value = true

        // 3. 发起请求
        streamJob = viewModelScope.launch {
            try {
                // 首次发消息时创建会话
                if (sessionId == null) {
                    sessionId = createSession()
                }

                val request = ChatRequest(
                    sessionId = sessionId,
                    message = content.trim(),
                    clientMessageId = UUID.randomUUID().toString()
                )

                val aiContent = StringBuilder()
                var aiMsgInserted = false

                ChatSseClient.streamChat(request).collect { delta ->
                    if (!aiMsgInserted) {
                        // 首个 token：替换思考占位为真正的 AI 气泡
                        val updated = _messages.value.orEmpty().toMutableList()
                        val thinkingIdx = updated.indexOfLast { it.type == Msg.TYPE_THINKING }
                        if (thinkingIdx >= 0) {
                            updated.removeAt(thinkingIdx)
                            updated.add(Msg(content = "", type = Msg.TYPE_RECEIVED))
                            aiMsgInserted = true
                            _messages.value = updated.toList()
                        }
                    }

                    // 追加 token 到 AI 消息内容
                    aiContent.append(delta)
                    val updated = _messages.value.orEmpty().toMutableList()
                    val aiIdx = updated.indexOfLast { it.type == Msg.TYPE_RECEIVED }
                    if (aiIdx >= 0) {
                        updated[aiIdx] = updated[aiIdx].copy(content = aiContent.toString())
                        _messages.value = updated.toList()
                    }
                }
            } catch (e: Exception) {
                // 异常：移除思考占位，追加错误消息
                Log.e("ChatViewModel", "流式请求失败", e)
                val errorDetail = when {
                    e.message != null -> e.message!!
                    e.cause?.message != null -> "原因: ${e.cause!!.message}"
                    else -> "未知错误(${e.javaClass.simpleName})"
                }
                val updated = _messages.value.orEmpty().toMutableList()
                val thinkingIdx = updated.indexOfLast { it.type == Msg.TYPE_THINKING }
                if (thinkingIdx >= 0) {
                    updated.removeAt(thinkingIdx)
                }
                updated.add(
                    Msg(
                        content = "抱歉，出了点问题：$errorDetail",
                        type = Msg.TYPE_RECEIVED
                    )
                )
                _messages.value = updated.toList()
            } finally {
                _isSending.value = false
            }
        }
    }

    /**
     * 通过 Retrofit 调用 POST /api/v1/chat/sessions 创建新会话。
     *
     * 为什么单独走 Retrofit 而不是 SSE？
     * 会话创建是一次性的普通 HTTP 请求，不需要流式响应。
     * 复用现有 RetrofitClient 的 token 拦截器和超时配置即可。
     */
    private suspend fun createSession(): Long {
        val response = RetrofitClient.apiService.createChatSession(
            ChatSessionRequest(title = null)
        )
        if (response.isSuccess && response.data != null) {
            return response.data.sessionId
        }
        throw Exception("创建会话失败：${response.message}")
    }

    /** 取消当前 SSE 流，Fragment 销毁时调用 */
    fun cancelStream() {
        streamJob?.cancel()
        streamJob = null
        _isSending.value = false
    }

    override fun onCleared() {
        super.onCleared()
        cancelStream()
    }

    /** Factory — 无外部依赖，直接实例化（参考项目中 CanteenListViewModel 的模式） */
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel() as T
        }
    }
}
