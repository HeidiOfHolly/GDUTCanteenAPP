package com.example.gdutcanteenapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.FragmentProfileHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class ProfileHomeFragment : BaseFragment<FragmentProfileHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileHomeBinding {
        return FragmentProfileHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.userLike.setOnClickListener {
            findNavController().navigate(R.id.action_profileHome_to_favoriteFragment)
        }
    }

    override fun observeData() {
    }
}
