package com.example.gdutcanteenapp.ui.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.gdutcanteenapp.data.remote.TokenManager
import com.example.gdutcanteenapp.ui.login.LoginActivity

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    open val isLoginPage: Boolean get() = false
    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.initialize(applicationContext)
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

    override fun onResume() {
        super.onResume()
        if (!isLoginPage && !TokenManager.isLoggedIn) {
            Toast.makeText(applicationContext, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    abstract fun getViewBinding(): VB?
    abstract fun initViews()
    abstract fun observeData()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}