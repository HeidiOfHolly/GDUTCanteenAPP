package com.example.gdutcanteenapp.ui.canteen.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.local.mock.MockDataProvider
import com.example.gdutcanteenapp.data.repositoryimpl.CanteenRepositoryImpl
import com.example.gdutcanteenapp.databinding.DishBrowseBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

// 菜品浏览页：某个食堂的所有窗口 + 每个窗口的菜品
class DishListFragment : BaseFragment<DishBrowseBinding>() {

    private lateinit var viewModel: DishListViewModel
    private lateinit var listAdapter: DishListAdapter

    private val canteenId: Int
        get() = arguments?.getInt(ARG_CANTEEN_ID) ?: 1

    private val canteenName: String
        // 每次读取 canteenName 时，都执行这里的代码
        get() = MockDataProvider.getMockCanteens()
            .firstOrNull { it.canteenId == canteenId }?.canteenName ?: ""
    //获得一个跟当前id一样的餐厅，不然就返回“”
    //返回集合中第一个元素 如果不是就返回

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): DishBrowseBinding {
        return DishBrowseBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        val db = AppDatabase.getInstance(requireContext())
        val repository = CanteenRepositoryImpl(db.canteenDao(), db.favouriteDao(), db.userDao())
        viewModel = ViewModelProvider(
            this, DishListViewModel.Factory(repository)
        )[DishListViewModel::class.java]

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

    override fun observeData() {
        viewModel.windowsWithDishes.observe(viewLifecycleOwner) { data ->
            listAdapter.submit(data)
        }
        viewModel.favoriteDishIds.observe(viewLifecycleOwner) { ids ->
            listAdapter.submit(listAdapter.currentData(), ids)
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
