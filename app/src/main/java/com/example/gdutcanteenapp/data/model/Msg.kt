package com.example.gdutcanteenapp.data.model

/**
 * 聊天消息数据模型。
 *
 * content: 消息文本内容
 * type:    消息类型 — TYPE_RECEIVED(AI消息/左气泡)、TYPE_SENT(用户消息/右气泡)、TYPE_THINKING(思考中占位)
 */
data class Msg(val content: String, val type: Int) {
    companion object {
        const val TYPE_RECEIVED = 0   // AI 回复消息（左对齐灰色气泡）
        const val TYPE_SENT = 1       // 用户发送消息（右对齐蓝色气泡）
        const val TYPE_THINKING = 2   // 思考中占位（用于流式开始前的加载动画）
    }
}