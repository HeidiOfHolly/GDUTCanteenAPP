package com.example.gdutcanteenapp.ui.base

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.example.gdutcanteenapp.R

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {
    var _binding: VB? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding?.root)
        initViews()
        observerDate()
    }

    abstract fun  getViewBinding(): VB?
    abstract fun initViews()
    abstract fun observerDate()
}