package com.example.gdutcanteenapp.ui.base.canteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.gdutcanteenapp.databinding.FragmentCanteenListBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class CanteenListFragment : BaseFragment<FragmentCanteenBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCanteenBinding {
        return ViewBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        // 初始化视图，例如设置 RecyclerView 的适配器等
    }

    override fun observerData() {
        // 观察数据变化，例如从 ViewModel 获取数据并更新 UI
    }
}