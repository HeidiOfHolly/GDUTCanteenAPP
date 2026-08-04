package com.example.gdutcanteenapp.ui.canteen.canteenlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.R
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
        adapter = CanteenListAdapter(onItemClick = { canteen ->
            val bundle = Bundle().apply { putInt("canteen_id", canteen.canteenId)
            putString("canteen_name", canteen.canteenName) }
            findNavController().navigate(
                R.id.action_fragment_canteen_list_to_fragment_dish_list,
                bundle
            )
        })
        binding.canteenListRv.adapter = adapter
    }

    override fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.canteenList.observe(viewLifecycleOwner) { canteens ->
            adapter.submitList(canteens)
        }
    }
}
