package com.example.gdutcanteenapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private var items: List<FavoriteDishItem>,
    private val onRemoveFavorite: (Int) -> Unit
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
        holder.binding.apply {
            tvDishName.text = item.dishName
            tvDishPrice.text = "￥${item.dishPrice}"
            tvDishTags.text = item.dishTags
            tvDishCanteen.text = item.canteenName
            tvDishWindow.text = item.windowName
            favoriteDish.setImageResource(R.drawable.check_like_foreground)
            favoriteDish.setOnClickListener {
                onRemoveFavorite(item.dishId)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submit(newItems: List<FavoriteDishItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
