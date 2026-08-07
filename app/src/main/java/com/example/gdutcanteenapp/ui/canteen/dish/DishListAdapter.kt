package com.example.gdutcanteenapp.ui.canteen.dish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.Window
import com.example.gdutcanteenapp.databinding.DishWindowBinding

// 窗口卡片纵向列表适配器
class DishListAdapter(
    private var data: List<Pair<Window, List<Dish>>>,
    private var favoriteDishIds: Set<Int> = emptySet(),
    private var onToggleFavorite: (Int) -> Unit = {}
) : RecyclerView.Adapter<DishListAdapter.WindowViewHolder>() {


    //缓存windowadapters，避免刷新
    private val windowAdapters = mutableMapOf<Int, WindowAdapter>()

    class WindowViewHolder(val binding: DishWindowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindowViewHolder {
        val binding = DishWindowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WindowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WindowViewHolder, position: Int) {
        //position是recyclerview自动传的参数 告诉你当前item在数据列表里的位置
        val (window, dishes) = data[position]
        holder.binding.windowName.text = window.windowName


        //保存当前的位置
        val adapter = WindowAdapter(dishes, favoriteDishIds, onToggleFavorite)
        windowAdapters[position] = adapter

        holder.binding.rvWindow.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            this.adapter = adapter
        }
    }

    override fun getItemCount(): Int = data.size

    fun submit(newData: List<Pair<Window, List<Dish>>>, newFavoriteIds: Set<Int>? = null) {
        data = newData
        windowAdapters.clear()
        if (newFavoriteIds != null) {
            favoriteDishIds = newFavoriteIds
        }
        notifyDataSetChanged()
    }


    //局部刷新的内在逻辑
    fun updateFavorites(newFavoriteIds: Set<Int>) {
        favoriteDishIds = newFavoriteIds
        windowAdapters.values.forEach { it.updateFavorites(newFavoriteIds) }
    }

}
