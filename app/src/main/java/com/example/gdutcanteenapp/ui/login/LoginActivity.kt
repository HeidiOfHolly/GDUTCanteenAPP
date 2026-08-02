package com.example.gdutcanteenapp.ui.login

import android.app.Dialog
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.ActivityLoginBinding
import com.example.gdutcanteenapp.ui.base.BaseActivity

class LoginActivity: BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(): ActivityLoginBinding? {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.tvRegisterText.setOnClickListener{
            showRegistrationDialog()
        }
    }

    override fun observeData() {
    }

    private fun showRegistrationDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_registration)
        dialog.show()
    }
}