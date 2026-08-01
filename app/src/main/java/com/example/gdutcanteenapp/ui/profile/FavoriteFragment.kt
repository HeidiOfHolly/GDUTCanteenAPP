package com.example.gdutcanteenapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.FragmentFavoriteBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, FavoriteViewModel.Factory(repository)
        )[FavoriteViewModel::class.java]

        adapter = FavoriteAdapter(emptyList()) { dishId ->
            viewModel.removeFavorite(dishId)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }
    }

    override fun observeData() {
        viewModel.favoriteItems.observe(viewLifecycleOwner) { items ->
            adapter.submit(items)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }
}
