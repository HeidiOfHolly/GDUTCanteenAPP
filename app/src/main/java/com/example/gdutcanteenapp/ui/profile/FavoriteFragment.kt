package com.example.gdutcanteenapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.FragmentFavoriteBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter
    private var currentItems: List<FavoriteDishItem> = emptyList()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            try {
                findNavController().navigateUp()
            } catch (e: IllegalStateException) {
                parentFragmentManager.popBackStack()
            }
        }

        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, FavoriteViewModel.Factory(repository)
        )[FavoriteViewModel::class.java]

        adapter = FavoriteAdapter(
            items = emptyList(),
            onToggleFavorite = { dishId -> viewModel.toggleFavorite(dishId) }
        )
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = this@FavoriteFragment.adapter
        }

    }

    override fun observeData() {
        viewModel.favoriteItems.observe(viewLifecycleOwner) { items ->
            currentItems = items
            adapter.submit(items)
        }
        viewModel.favoriteDishIds.observe(viewLifecycleOwner) { ids ->
            adapter.submit(currentItems, ids)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            val keyword = arguments?.getString(ARG_KEYWORD)
            if (!keyword.isNullOrEmpty()) {
                viewModel.searchDishes(keyword)
            } else {
                viewModel.load()
            }
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
