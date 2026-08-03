package com.example.gdutcanteenapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.databinding.FragmentChatBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

/**
 * AI 对话聊天页面。
 *
 * 职责：管理 RecyclerView（聊天气泡列表）和底部输入栏，
 * 将用户输入传递给 ChatViewModel，观察 LiveData 并刷新 UI。
 *
 * 布局：fragment_chat.xml
 *   - RecyclerView（气泡列表，stackFromEnd = true 实现底部对齐）
 *   - 输入栏：EditText + 发送按钮（发送中时置灰禁用）
 */
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        // ViewModel — 使用 Factory 模式（与项目其他 ViewModel 一致）
        viewModel = ViewModelProvider(this, ChatViewModel.Factory())[ChatViewModel::class.java]

        // RecyclerView：从底部开始堆叠（聊天界面标准行为）
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager

        adapter = ChatAdapter()
        binding.recyclerView.adapter = adapter

        // 发送按钮：取输入文字 → ViewModel 处理 → 清空输入框
        binding.ivSend.setOnClickListener {
            val text = binding.etInput.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendMessage(text)
                binding.etInput.text?.clear()
            }
        }
    }

    override fun observeData() {
        // 观察消息列表 — 流式接收时 LiveData 频繁更新，逐字刷新聊天气泡
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
            // 智能自动滚动：仅在用户接近底部时才自动滚动，避免打断查看历史消息
            val lm = binding.recyclerView.layoutManager as LinearLayoutManager
            val lastVisible = lm.findLastVisibleItemPosition()
            val totalItems = adapter.itemCount
            if (totalItems > 0 && lastVisible >= totalItems - 2) {
                binding.recyclerView.smoothScrollToPosition(totalItems - 1)
            }
        }

        // 观察发送状态 — 发送中禁用按钮，防止重复发送
        viewModel.isSending.observe(viewLifecycleOwner) { isSending ->
            binding.ivSend.isEnabled = !isSending
            binding.ivSend.alpha = if (isSending) 0.4f else 1.0f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment 销毁时取消 SSE 连接，避免内存泄漏和无效网络请求
        viewModel.cancelStream()
    }
}
