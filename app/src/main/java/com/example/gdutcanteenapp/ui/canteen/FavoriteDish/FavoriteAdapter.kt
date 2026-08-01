package com.example.gdutcanteenapp.ui.canteen.FavoriteDish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.databinding.ItemFavoriteBinding


class FavoriteAdapter(
    private var items: List<FavoriteDishItem> = emptyList()
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>()  {
    class FavoriteViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvDishName.text = item.dish.dishName
        holder.binding.tvDishPrice.text = item.dish.dishPrice
        holder.binding.tvDishTags.text = item.dish.dishTags
        holder.binding.tvDishCanteen.text = item.canteenName
        holder.binding.tvDishWindow.text = item.windowName
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<FavoriteDishItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
