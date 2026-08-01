package com.example.gdutcanteenapp.ui.canteen.FavoriteDish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.FragmentFavoriteBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var listAdapter: FavoriteAdapter

    private val keyword: String?
        get() = arguments?.getString(ARG_KEYWORD)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, FavoriteViewModel.Factory(repository)
        )[FavoriteViewModel::class.java]

        listAdapter = FavoriteAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 带 keyword 进来 → 搜索；否则 → 收藏列表
        val keyword = arguments?.getString(ARG_KEYWORD)
        if (!keyword.isNullOrEmpty()) {
            viewModel.searchDishes(keyword)
        } else {
            viewModel.loadDishes()
        }
    }

    override fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            listAdapter.submitList(items)
        }
    }

    companion object {
        private const val ARG_KEYWORD = "keyword"

        fun newInstanceByKeyword(keyword: String): FavoriteFragment {
            return FavoriteFragment().apply {
                arguments = bundleOf(ARG_KEYWORD to keyword)
            }
        }
    }
}
