package com.example.gdutcanteenapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private var items: List<FavoriteDishItem>,
    private val onToggleFavorite: (Int) -> Unit,
    private var favoriteDishIds: Set<Int> = emptySet()
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isFav = item.dishId in favoriteDishIds
        holder.binding.apply {
            tvDishName.text = item.dishName
            tvDishPrice.text = "￥${item.dishPrice}"
            tvDishTags.text = item.dishTags
            tvDishCanteen.text = item.canteenName
            tvDishWindow.text = item.windowName
            favoriteDish.setImageResource(
                if (isFav) R.drawable.like2 else R.drawable.like1
            )
            favoriteDish.setOnClickListener {
                onToggleFavorite(item.dishId)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submit(newItems: List<FavoriteDishItem>, newFavoriteIds: Set<Int>? = null) {
        items = newItems
        if (newFavoriteIds != null) {
            favoriteDishIds = newFavoriteIds
        }
        notifyDataSetChanged()
    }
}
