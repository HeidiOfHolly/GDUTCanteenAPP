package com.example.gdutcanteenapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.FragmentCanteenBinding
import com.example.gdutcanteenapp.databinding.FragmentHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.ui.canteen.FavoriteDish.FavoriteFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        // Initialize your views here
    }

    override fun observeData() {
        binding.tvSearchBtn.setOnClickListener() {
            val keyword = binding.etSearch.text.toString()
            if (keyword.isEmpty()) {
                Toast.makeText(requireContext(), "你想食咩啊，话比我知啦", Toast.LENGTH_SHORT).show()
            } else {
                val favoriteFragment = FavoriteFragment.newInstanceByKeyword(keyword)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.home_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}