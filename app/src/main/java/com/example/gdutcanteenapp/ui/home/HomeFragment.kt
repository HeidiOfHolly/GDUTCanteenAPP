package com.example.gdutcanteenapp.ui.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.FragmentHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.ui.profile.FavoriteAdapter
import com.example.gdutcanteenapp.ui.profile.FavoriteDishItem
import com.example.gdutcanteenapp.ui.profile.FavoriteFragment
import com.google.android.material.chip.Chip

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var isHistoryVisible = false
    private lateinit var historyManager: SearchHistoryManager
    private var backStackListener: FragmentManager.OnBackStackChangedListener? = null
    private lateinit var viewModel: RecommendViewModel
    private lateinit var adapter: FavoriteAdapter
    private var currentItems: List<FavoriteDishItem> = emptyList()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        historyManager = SearchHistoryManager(requireContext())
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                isHistoryVisible = true
                binding.historyContainer.visibility = View.VISIBLE
                showHistory()
            } else {
                isHistoryVisible = false
                binding.historyContainer.visibility = View.GONE
            }
        }
        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, RecommendViewModel.Factory(repository)
        )[RecommendViewModel::class.java]

        adapter = FavoriteAdapter(
            items = emptyList(),
            onToggleFavorite = { dishId -> viewModel.toggleFavorite(dishId) }
        )
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = this@HomeFragment.adapter
        }

        // 搜索结果显示时隐藏首页自身的控件，返回（back stack 清空）时恢复。
        // 用监听器而非 onResume：替换进来的 Fragment 与 HomeFragment 同生命周期，返回时 onResume 不会触发。
        backStackListener = FragmentManager.OnBackStackChangedListener {
            updateHomeContentVisibility()
        }
        childFragmentManager.addOnBackStackChangedListener(backStackListener!!)
        updateHomeContentVisibility()
    }

    override fun onResume() {
        super.onResume()
        // 切回首页 tab 时刷新收藏状态，避免与其他页面的操作不同步
        if (::viewModel.isInitialized) {
            viewModel.refreshFavoriteStates()
        }
    }

    override fun onDestroyView() {
        backStackListener?.let { childFragmentManager.removeOnBackStackChangedListener(it) }
        backStackListener = null
        super.onDestroyView()
    }

    override fun observeData() {
        viewModel.recommendDishes.observe(viewLifecycleOwner) { items ->
            currentItems = items
            adapter.submit(items)
        }
        viewModel.favoriteDishIds.observe(viewLifecycleOwner) { ids ->
            adapter.submit(currentItems, ids)
        }
        viewModel.loadRecommendDishes()

        binding.tvSearchBtn.setOnClickListener() {
            val keyword = binding.etSearch.text.toString()
            if (keyword.isEmpty()) {
                Toast.makeText(requireContext(), "你想食咩啊，话比我知啦", Toast.LENGTH_SHORT).show()
            } else {
                historyManager.addHistory(keyword)
                val favoriteFragment = FavoriteFragment.newInstanceByKeyword(keyword)
                // 必须用 childFragmentManager 而非 parentFragmentManager：
                // ViewPager2 切页只限制它管理的 tab Fragment（HomeFragment）到 STARTED，
                // 挂在 Activity FragmentManager 上的子 Fragment 不受限，切走时不会进 onPause，
                // 切回时 onResume 不触发，搜索结果列表不会刷新。
                // 用 childFragmentManager 后子 Fragment 生命周期跟随 HomeFragment，切回即刷新。
                childFragmentManager.beginTransaction()
                    .replace(R.id.home_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // 首页控件在搜索结果页打开时隐藏，back stack 清空后恢复，让结果页覆盖整屏
    private fun updateHomeContentVisibility() {
        val searchShowing = childFragmentManager.backStackEntryCount > 0
        binding.toolbar.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.ivDish.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.tvRecommend.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.recyclerView.visibility = if (searchShowing) View.GONE else View.VISIBLE
        // historyContainer 始终保持初始的隐藏状态，避免搜索结果页残留历史面板
        binding.historyContainer.visibility = View.GONE
        // 从搜索结果页退回首页时清空搜索栏（无论是否搜到结果）
        if (!searchShowing) {
            binding.etSearch.setText("")
        }
    }

    private fun showHistory() {
        val history = historyManager.getHistory()
        if (history.isEmpty()) {
            isHistoryVisible = false
            binding.historyContainer.visibility = View.GONE
            return
        }
        binding.historyContainer.visibility = View.VISIBLE
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        val history = historyManager.getHistory()
        binding.chipGroup.removeAllViews()

        history.forEach { record ->
            val chip = createChip(record)
            binding.chipGroup.addView(chip)
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(requireContext()).apply {
            setText(text)
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.yellow5))
            isCloseIconVisible = true
            setOnClickListener {
                binding.etSearch.setText(text)
                binding.etSearch.setSelection(text.length)
                isHistoryVisible = false
                // 点击历史记录直接触发搜索（与点击搜索按钮走同一流程）
                val favoriteFragment = FavoriteFragment.newInstanceByKeyword(text)
                childFragmentManager.beginTransaction()
                    .replace(R.id.home_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit()
            }
            setOnCloseIconClickListener {
                historyManager.removeHistory(text)
                showHistory()
            }
        }
    }



}