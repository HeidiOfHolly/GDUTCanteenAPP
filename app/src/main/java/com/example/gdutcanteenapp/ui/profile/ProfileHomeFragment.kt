package com.example.gdutcanteenapp.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.data.remote.TokenManager
import com.example.gdutcanteenapp.databinding.FragmentProfileHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.ui.chat.ChatViewModel
import com.example.gdutcanteenapp.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileHomeFragment : BaseFragment<FragmentProfileHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileHomeBinding {
        return FragmentProfileHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.userName.text = "用户昵称：${TokenManager.getUserName()}"
        binding.userId.text = "用户账号：${TokenManager.getUserId()}"

        binding.userLike.setOnClickListener {
            findNavController().navigate(R.id.action_profileHome_to_favoriteFragment)
        }
        binding.logOut.setOnClickListener {
            showLogOutConfirmDialog()
        }
        binding.deleteChat.setOnClickListener {
                showAIConfirmDialog()
        }
    }


    override fun observeData() {
    }

    private fun showAIConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("提示")
            .setMessage("确认清空AI对话历史吗？")
            .setPositiveButton("是") { _, _ ->
                // 点击"是"的逻辑
                val chatViewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
                chatViewModel.clearHistory()
                Toast.makeText(requireContext(), "已清空所有历史对话", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("否") { _, _ ->
                // 点击"否"的逻辑

            }
            .show()
    }

    private fun showLogOutConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("提示")
            .setMessage("确认要退出登录吗？")
            .setPositiveButton("是") { _, _ ->
                TokenManager.clearToken()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("否") { _, _ ->
                // 点击"否"的逻辑

            }
            .show()
    }
}
