package com.example.gdutcanteenapp.ui.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        val origLeft = binding.root.paddingLeft
        val origRight = binding.root.paddingRight
        val origBottom = binding.root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(
                origLeft + statusBars.left,
                statusBars.top,
                origRight + statusBars.right,
                origBottom + navBars.bottom
            )
            insets
        }

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