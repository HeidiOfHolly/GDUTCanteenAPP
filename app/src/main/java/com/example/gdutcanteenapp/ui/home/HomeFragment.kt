package com.example.gdutcanteenapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.gdutcanteenapp.databinding.FragmentCanteenBinding
import com.example.gdutcanteenapp.databinding.FragmentHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        // Initialize your views here
    }

    override fun observeData() {
        // Observe your data here
    }

}