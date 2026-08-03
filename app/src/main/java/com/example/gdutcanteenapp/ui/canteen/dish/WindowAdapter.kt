package com.example.gdutcanteenapp.ui.canteen.dish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.databinding.DishItemBinding

// 菜品横向列表适配器
class WindowAdapter(
    private val dishes: List<Dish>,
    private var favoriteDishIds: Set<Int>,
    private val onToggleFavorite: (Int) -> Unit
) : RecyclerView.Adapter<WindowAdapter.DishViewHolder>() {

    fun updateFavorites(newFavoriteIds: Set<Int>) {
        favoriteDishIds = newFavoriteIds
        notifyItemRangeChanged(0, dishes.size)
    }

    class DishViewHolder(val binding: DishItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = DishItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.binding.dishName.text = dish.dishName
        if (dish.dishPrice.isNotEmpty()){
            holder.binding.dishPrice.text = "￥${dish.dishPrice}"
        }else{
            holder.binding.dishPrice.text = "￥ 称重计价"
        }
        holder.binding.dishTag.text = formatTags(dish.dishTags)

        val isFav = dish.dishId in favoriteDishIds
        holder.binding.likeIt.setImageResource(
            if (isFav) com.example.gdutcanteenapp.R.drawable.check_like_foreground
            else com.example.gdutcanteenapp.R.drawable.ic_like_foreground
        )
        holder.binding.likeIt.setOnClickListener {
            onToggleFavorite(dish.dishId)
        }
    }

    override fun getItemCount(): Int = dishes.size

    // 把 ["招牌","热门"] 这样的 JSON 字符串转成 招牌，热门
    private fun formatTags(raw: String): String {
        return raw.trim()//去除首尾的空白字符
            .removePrefix("[").removeSuffix("]")//去除开头和结尾的[]
            .split(",")//按，，分割字符串列表
            .map { it.trim().trim('"') }//去除每个元素的首尾空格和双引号并组合起来
            .filter { it.isNotEmpty() }//过滤掉空字符串
            .joinToString("，")//用中文逗号连接所有元素
    }
}
