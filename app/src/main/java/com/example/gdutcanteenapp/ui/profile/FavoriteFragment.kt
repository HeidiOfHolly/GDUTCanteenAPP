package com.example.gdutcanteenapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.FragmentFavoriteBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            navigateBack()
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

        binding.btnRetryFavorite.setOnClickListener {
            val keyword = arguments?.getString(ARG_KEYWORD)
            if (!keyword.isNullOrEmpty()) {
                viewModel.searchDishes(keyword)
            } else {
                viewModel.load()
            }
        }
    }

    override fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.favoriteItems.observe(viewLifecycleOwner) { items ->
            currentItems = items
            adapter.submit(items)
            val isEmpty = items.isEmpty()
            binding.tvEmptyFavorite.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
            // 搜索模式（带 keyword）下结果为空 → 弹窗引导返回首页；收藏页（无 keyword）不提示
            val keyword = arguments?.getString(ARG_KEYWORD)
            if (!keyword.isNullOrEmpty() && items.isEmpty()) {
                showEmptyResultDialog()
            }
        }
        viewModel.favoriteDishIds.observe(viewLifecycleOwner) { ids ->
            adapter.submit(currentItems, ids)
        }
        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            binding.errorContainer.visibility = if (isError) View.VISIBLE else View.GONE
            binding.tvEmptyFavorite.visibility = View.GONE
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

    private var emptyResultDialog: AlertDialog? = null

    /** 返回上一页：兼容导航组件 NavHost 内和 Fragment 事务两种添加方式 */
    private fun navigateBack() {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalStateException) {
            parentFragmentManager.popBackStack()
        }
    }

    /** 搜索无结果时弹窗，点击"是"返回首页 */
    private fun showEmptyResultDialog() {
        if (emptyResultDialog?.isShowing == true) return
        emptyResultDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("未找到搜索结果，点击下方按钮退回到首页")
            .setPositiveButton("是") { _, _ -> navigateBack() }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emptyResultDialog?.dismiss()
        emptyResultDialog = null
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
