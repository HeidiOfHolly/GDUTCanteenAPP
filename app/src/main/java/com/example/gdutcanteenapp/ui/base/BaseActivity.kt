package com.example.gdutcanteenapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        initViews()
        observeData()
    }

    abstract fun getViewBinding(): VB?
    abstract fun initViews()
    abstract fun observeData()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}