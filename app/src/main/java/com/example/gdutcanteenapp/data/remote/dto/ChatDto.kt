package com.example.gdutcanteenapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 聊天请求 DTO — 对应接口文档 POST /api/v1/chat 的请求体。
 *
 * 设计思路：
 * - 后端通过 sessionId 管理对话上下文（服务端存储历史），
 *   客户端每次只需发送最新一条用户消息，而非像 OpenAI 那样每次传完整 messages 数组。
 * - sessionId 为空时后端自动创建新会话，后续发消息时带上 sessionId 继续对话。
 * - clientMessageId 由客户端生成 UUID，用于去重和幂等。
 */
data class ChatRequest(
    @SerializedName("sessionId")
    val sessionId: Long? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("clientMessageId")
    val clientMessageId: String
)

// ═══════════════════════════════════════════════════════════
// 以下为 SSE 响应解析结构
// 接口文档中 SSE 响应类型为 SseEmitter，未明确标注具体 JSON 格式。
// 因此采用两阶段解析策略（ChatSseClient.parseSseContent）：
//   1. 先尝试 OpenAI 兼容格式：choices[0].delta.content
//   2. 失败则提取 JSON 根对象的 "content" 字段
// 无论后端选择哪种格式，都能正确解析。
// ═══════════════════════════════════════════════════════════

data class ChatStreamChunk(
    @SerializedName("choices")
    val choices: List<ChoiceDto>?
) {
    /** 提取本 chunk 中的文本增量（token），若格式不匹配则返回 null */
    val contentDelta: String?
        get() = choices?.firstOrNull()?.delta?.content
}

data class ChoiceDto(
    @SerializedName("delta")
    val delta: DeltaDto?
)

data class DeltaDto(
    @SerializedName("content")
    val content: String?
)

// ═══════════════════════════════════════════════════════════
// 会话管理 DTO — 对应 POST /api/v1/chat/sessions 的响应
// ═══════════════════════════════════════════════════════════

/** 创建会话的请求体（title 可选） */
data class ChatSessionRequest(
    @SerializedName("title")
    val title: String? = null
)

/** 会话信息响应 */
data class ChatSessionResponse(
    @SerializedName("sessionId")
    val sessionId: Long,
    @SerializedName("title")
    val title: String?,
    @SerializedName("createdAt")
    val createdAt: String?
)
