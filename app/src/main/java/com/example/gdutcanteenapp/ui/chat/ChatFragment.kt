package com.example.gdutcanteenapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.gdutcanteenapp.databinding.FragmentChatBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class ChatFragment:BaseFragment<FragmentChatBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }
    override fun initViews() {
    }

    override fun observeData() {
    }
}