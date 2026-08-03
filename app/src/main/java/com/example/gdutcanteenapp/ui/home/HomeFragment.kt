package com.example.gdutcanteenapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.FragmentHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.ui.profile.FavoriteFragment
import com.google.android.material.chip.Chip

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var isHistoryVisible = false
    private lateinit var historyManager: SearchHistoryManager
    private var backStackListener: FragmentManager.OnBackStackChangedListener? = null

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
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = this.adapter
        }

        // 搜索结果显示时隐藏首页自身的控件，返回（back stack 清空）时恢复。
        // 用监听器而非 onResume：替换进来的 Fragment 与 HomeFragment 同生命周期，返回时 onResume 不会触发。
        backStackListener = FragmentManager.OnBackStackChangedListener {
            updateHomeContentVisibility()
        }
        parentFragmentManager.addOnBackStackChangedListener(backStackListener!!)
        updateHomeContentVisibility()
    }

    override fun onDestroyView() {
        backStackListener?.let { parentFragmentManager.removeOnBackStackChangedListener(it) }
        backStackListener = null
        super.onDestroyView()
    }

    override fun observeData() {
        binding.tvSearchBtn.setOnClickListener() {
            val keyword = binding.etSearch.text.toString()
            if (keyword.isEmpty()) {
                Toast.makeText(requireContext(), "你想食咩啊，话比我知啦", Toast.LENGTH_SHORT).show()
            } else {
                historyManager.addHistory(keyword)
                val favoriteFragment = FavoriteFragment.newInstanceByKeyword(keyword)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.home_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // 首页控件在搜索结果页打开时隐藏，back stack 清空后恢复，让结果页覆盖整屏
    private fun updateHomeContentVisibility() {
        val searchShowing = parentFragmentManager.backStackEntryCount > 0
        binding.toolbar.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.ivDish.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.tvRecommend.visibility = if (searchShowing) View.GONE else View.VISIBLE
        binding.recyclerView.visibility = if (searchShowing) View.GONE else View.VISIBLE
        // historyContainer 始终保持初始的隐藏状态，避免搜索结果页残留历史面板
        binding.historyContainer.visibility = View.GONE
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
            isCloseIconVisible = true
            setOnClickListener {
                binding.etSearch.setText(text)
                binding.etSearch.setSelection(text.length)
                isHistoryVisible = false
                binding.historyContainer.visibility = View.GONE
            }
            setOnCloseIconClickListener {
                historyManager.removeHistory(text)
                showHistory()
            }
        }
    }



}