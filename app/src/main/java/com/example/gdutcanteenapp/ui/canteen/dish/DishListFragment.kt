package com.example.gdutcanteenapp.ui.canteen.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.DishBrowseBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 菜品浏览页：某个食堂的所有窗口 + 每个窗口的菜品
class DishListFragment : BaseFragment<DishBrowseBinding>() {

    private lateinit var viewModel: DishListViewModel
    private lateinit var listAdapter: DishListAdapter

    private val canteenId: Int
        get() = arguments?.getInt(ARG_CANTEEN_ID) ?: 1

    private var canteenName: String = ""

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): DishBrowseBinding {
        return DishBrowseBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, DishListViewModel.Factory(repository)
        )[DishListViewModel::class.java]

        // 从 API / 本地缓存加载食堂名称
        binding.toolbar.title = ""
        lifecycleScope.launch {
            canteenName = withContext(Dispatchers.IO) {
                repository.getCanteenById(canteenId)?.canteenName ?: ""
            }
            binding.toolbar.title = canteenName
        }

        listAdapter = DishListAdapter(
            data = emptyList(),
            canteenName = canteenName,
            onToggleFavorite = { dishId -> viewModel.toggleFavorite(dishId) }
        )
        binding.rvBrowse.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        viewModel.load(canteenId)
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.refreshFavorites()
        }
    }

    override fun observeData() {
        viewModel.windowsWithDishes.observe(viewLifecycleOwner) { data ->
            listAdapter.submit(data)
        }
        viewModel.favoriteDishIds.observe(viewLifecycleOwner) { ids ->
            listAdapter.updateFavorites(ids)
        }
    }

    companion object {
        private const val ARG_CANTEEN_ID = "canteen_id"

        fun newInstance(canteenId: Int): DishListFragment {
            return DishListFragment().apply {
                arguments = Bundle().apply { putInt(ARG_CANTEEN_ID, canteenId) }
            }
        }
    }
}
