package com.example.gdutcanteenapp.ui.chat

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.data.model.Msg

/**
 * 聊天消息 RecyclerView 适配器。
 *
 * 支持三种 ViewType：
 * - TYPE_SENT (1)     → 右对齐蓝色气泡（用户消息）
 * - TYPE_RECEIVED (0) → 左对齐灰色气泡（AI 回复）
 * - TYPE_THINKING (2) → 左对齐 "正在思考…" 占位气泡（等待 AI 首个 token）
 *
 * 设计思路：
 * - 使用 getItemViewType() 根据 Msg.type 返回不同布局，实现聊天气泡左右区分
 * - submitList() 每次全量刷新整个列表。流式场景下 ViewModel 每次收到新 token
 *   都会构建新的 List 并调用此方法，RecyclerView 自动 diff 更新，实现逐字打字机效果
 */
class ChatAdapter(private var msgList: List<Msg> = emptyList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** 暴露当前消息列表，供 Fragment 追加消息时读取 */
    val currentList: List<Msg> get() = msgList

    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftMsg: TextView = view.findViewById(R.id.tv_left)
    }

    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rightMsg: TextView = view.findViewById(R.id.tv_right)
    }

    /** 思考中占位 ViewHolder — 布局是纯静态的，无需绑定数据 */
    inner class ThinkingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Msg.TYPE_RECEIVED -> {
                val view = inflater.inflate(R.layout.item_message_l, parent, false)
                LeftViewHolder(view)
            }
            Msg.TYPE_SENT -> {
                val view = inflater.inflate(R.layout.litem_message_r, parent, false)
                RightViewHolder(view)
            }
            Msg.TYPE_THINKING -> {
                val view = inflater.inflate(R.layout.item_chat_thinking, parent, false)
                ThinkingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            is ThinkingViewHolder -> { /* 思考中气泡是纯静态的，无需绑定数据 */ }
            else -> throw IllegalArgumentException("Unknown ViewHolder type")
        }
    }

    override fun getItemCount() = msgList.size

    /**
     * 更新消息列表并刷新整个 RecyclerView。
     * 流式接收时调用此方法，每次收到新 token 都会触发 UI 刷新，实现逐字打字机效果。
     */
    fun submitList(newList: List<Msg>) {
        msgList = newList
        notifyDataSetChanged()
    }
}
