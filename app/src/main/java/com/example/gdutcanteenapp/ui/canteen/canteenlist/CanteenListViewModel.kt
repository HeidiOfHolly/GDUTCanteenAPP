package com.example.gdutcanteenapp.ui.canteen.canteenlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.data.remote.TokenManager
import kotlinx.coroutines.launch

class CanteenListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CanteenRepositoryImpl(
        AppDatabase.getInstance(application).canteenDao(),
        AppDatabase.getInstance(application).favouriteDao(),
        AppDatabase.getInstance(application).userDao()
    )

    private val _canteenList = MutableLiveData<List<Canteen>>()
    val canteenList: LiveData<List<Canteen>> get() = _canteenList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    init {
        loadCanteens()
    }

    private fun loadCanteens() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertUser(User(userId = TokenManager.getUserId(), userName = TokenManager.getUserName(), userAccount = TokenManager.getUserId(), userPassword = ""))
            _canteenList.value = repository.getAllCanteens()
            _isLoading.value = false
        }
    }
}
