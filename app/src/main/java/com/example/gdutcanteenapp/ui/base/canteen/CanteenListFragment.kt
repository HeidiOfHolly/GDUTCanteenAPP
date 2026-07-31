package com.example.gdutcanteenapp.ui.base.canteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.databinding.FragmentCanteenListBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

class CanteenListFragment : BaseFragment<FragmentCanteenListBinding>() {

    val viewModel by lazy { ViewModelProvider(this).get(CanteenListViewModel::class.java) }
    private lateinit var adapter: CanteenListAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCanteenListBinding {
        return FragmentCanteenListBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.canteenListRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = CanteenListAdapter()
        binding.canteenListRv.adapter = adapter
    }

    override fun observeData() {
        viewModel.canteenList.observe(viewLifecycleOwner) { canteens ->
            adapter.submitList(canteens)
        }
    }
}
