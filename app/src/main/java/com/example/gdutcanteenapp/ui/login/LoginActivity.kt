package com.example.gdutcanteenapp.ui.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gdutcanteenapp.MainActivity
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.ActivityLoginBinding
import com.example.gdutcanteenapp.databinding.DialogRegistrationBinding
import com.example.gdutcanteenapp.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var viewModel: RegistrationViewModel
    private var registerDialog: Dialog? = null
    // 注册成功后回填登录页学号，用字段暂存避免回调中丢失
    private var registerPendingAccount: String = ""

    override fun getViewBinding(): ActivityLoginBinding? {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        val repository = CanteenRepositoryImpl(
            AppDatabase.getInstance(this).canteenDao(),
            AppDatabase.getInstance(this).favouriteDao(),
            AppDatabase.getInstance(this).userDao()
        )
        viewModel = ViewModelProvider(
            this, RegistrationViewModel.Factory(repository)
        )[RegistrationViewModel::class.java]

        loadSavedCredentials()

        binding.tvRegisterText.setOnClickListener {
            showRegisterDialog()
        }

        binding.bnLogin.setOnClickListener {
            val account = binding.etStudentNo.text.toString().trim()
            val password = binding.etPassword.text.toString()
            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入学号和密码", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(account, password)
            }
        }
    }

    override fun observeData() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is AuthState.Success -> {
                    if (binding.cbRememberPass.isChecked) {
                        saveCredentials(
                            binding.etStudentNo.text.toString().trim(),
                            binding.etPassword.text.toString()
                        )
                    } else {
                        clearCredentials()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthState.Error -> Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        viewModel.registerState.observe(this) { state ->
            when (state) {
                is AuthState.Success -> {
                    registerDialog?.dismiss()
                    registerDialog = null
                    binding.etStudentNo.setText(
                        registerPendingAccount
                    )
                    Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Error -> Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    private fun showRegisterDialog() {
        val binding = DialogRegistrationBinding.inflate(layoutInflater)
        val dialog = Dialog(this).apply {
            setContentView(binding.root)
            setCancelable(true)
        }

        binding.registerButton.setOnClickListener {
            val account = binding.registerUserAccount.text.toString().trim()
            val username = binding.registerUserName.text.toString().trim()
            val password = binding.registerPassword.text.toString()
            val passwordRepeat = binding.registerPasswordRepeat.text.toString()
            registerPendingAccount = account
            viewModel.register(account, username, password, passwordRepeat)
        }

        binding.loginText.setOnClickListener {
            dialog.dismiss()
            registerDialog = null
        }

        registerDialog = dialog
        dialog.show()
    }

    private fun prefs() = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    private fun loadSavedCredentials() {
        val account = prefs().getString("student_no", null)
        val password = prefs().getString("password", null)
        if (!account.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding.etStudentNo.setText(account)
            binding.etPassword.setText(password)
            binding.cbRememberPass.isChecked = true
        }
    }

    private fun saveCredentials(account: String, password: String) {
        prefs().edit().apply {
            putString("student_no", account)
            putString("password", password)
        }.apply()
    }

    private fun clearCredentials() {
        prefs().edit().apply {
            remove("student_no")
            remove("password")
        }.apply()
    }
}
