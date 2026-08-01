package com.example.gdutcanteenapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.FragmentHomeBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import com.example.gdutcanteenapp.ui.canteen.FavoriteDish.FavoriteFragment
import com.google.android.material.chip.Chip

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var isHistoryVisible = false
    private lateinit var historyManager: SearchHistoryManager

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        historyManager = SearchHistoryManager(requireContext())
        binding.etSearch.setOnClickListener{ hasFocus ->
            if( hasFocus && !isHistoryVisible ) {
                isHistoryVisible = true
                binding.historyContainer.visibility = android.view.View.VISIBLE
                showHistory()
            } else {
                isHistoryVisible = false
                binding.historyContainer.visibility = android.view.View.GONE
            }
        }
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

    private fun showHistory() {
        if (historyManager.getHistory().isEmpty()) return
        isHistoryVisible = true
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
        }
        }



}