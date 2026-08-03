package com.example.gdutcanteenapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class RegistrationViewModel(
    private val repository: CanteenRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<AuthState>(AuthState.Idle)
    val loginState: LiveData<AuthState> = _loginState

    private val _registerState = MutableLiveData<AuthState>(AuthState.Idle)
    val registerState: LiveData<AuthState> = _registerState

    fun login(account: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.login(account, password)
                _loginState.value = AuthState.Success
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "学号或密码错误")
            }
        }
    }

    // 校验失败返回 false，并通过 registerState 携带错误信息
    fun register(account: String, username: String, password: String, passwordRepeat: String): Boolean {
        val error = validate(account, username, password, passwordRepeat)
        if (error != null) {
            _registerState.value = AuthState.Error(error)
            return false
        }
        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                repository.register(account, username, password)
                _registerState.value = AuthState.Success
            } catch (e: Exception) {
                _registerState.value = AuthState.Error(e.message ?: "注册失败")
            }
        }
        return true
    }

    private fun validate(account: String, username: String, password: String, passwordRepeat: String): String? {
        return when {
            account.isEmpty() || username.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() ->
                "请填写完整信息"
            username.length > 12 ||username.length <2 ->
                "用户名应在2-12个字符之间"
            account.length>20 || account.length<6 ->
                "学号格式不对"
            password.length < 8 || password.length > 20 ->
                "密码长度应在8-20位之间"
            password != passwordRepeat ->
                "两次输入的密码不一致"
            validatePassword(password).not() ->
                "密码必须包含字母和数字"
            else -> null
        }
    }

    fun validatePassword(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    class Factory(
        private val repository: CanteenRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegistrationViewModel(repository) as T
        }
    }
}
