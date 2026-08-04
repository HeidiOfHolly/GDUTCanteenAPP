package com.example.gdutcanteenapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gdutcanteenapp.data.repository.CanteenRepository
import com.example.gdutcanteenapp.ui.profile.FavoriteDishItem

class RecommendViewModel(private val repository: CanteenRepository): ViewModel() {

//    val recommendDishes : LiveData<Set<Int>> = _recommendDishes
//    private val _recommendDishes =  MutableLiveData<Set<Int>>(emptySet())
//
//    val recommendDishesItems : LiveData<List<FavoriteDishItem>> = _recommendDishesItems
//
//    fun loadRecommendDishes() {
//
//    }
}