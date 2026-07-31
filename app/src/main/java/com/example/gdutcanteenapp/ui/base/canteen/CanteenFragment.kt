package com.example.gdutcanteenapp.ui.base.canteen

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.gdutcanteenapp.databinding.FragmentCanteenBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class CanteenFragment : BaseFragment<FragmentCanteenBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCanteenBinding {
        return FragmentCanteenBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        // 初始化视图，例如设置 RecyclerView 的适配器等
    }

    override fun observeData() {
    }
}