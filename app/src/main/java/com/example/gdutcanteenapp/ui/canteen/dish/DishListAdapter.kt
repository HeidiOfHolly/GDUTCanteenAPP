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
    private val canteenName: String,
    private var favoriteDishIds: Set<Int> = emptySet(),
    private var onToggleFavorite: (Int) -> Unit = {}
) : RecyclerView.Adapter<DishListAdapter.WindowViewHolder>() {

    class WindowViewHolder(val binding: DishWindowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindowViewHolder {
        val binding = DishWindowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WindowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WindowViewHolder, position: Int) {
        val (window, dishes) = data[position]
        holder.binding.windowName.text = window.windowName

        holder.binding.rvWindow.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = WindowAdapter(dishes, favoriteDishIds, onToggleFavorite)
        }
    }

    override fun getItemCount(): Int = data.size

    fun submit(newData: List<Pair<Window, List<Dish>>>, newFavoriteIds: Set<Int>? = null) {
        data = newData
        if (newFavoriteIds != null) {
            favoriteDishIds = newFavoriteIds
        }
        notifyDataSetChanged()
    }

    fun currentData(): List<Pair<Window, List<Dish>>> = data
}
