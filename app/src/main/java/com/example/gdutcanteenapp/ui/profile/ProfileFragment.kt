package com.example.gdutcanteenapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(){

    val viewModel by lazy { ViewModelProvider(this).get() }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater,container,false)
    }

    override fun initViews() {

    }

    override fun observeData() {

    }

}