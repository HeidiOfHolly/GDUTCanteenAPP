package com.example.gdutcanteenapp.ui.canteen.canteenlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.databinding.ItemCanteenBinding

class CanteenListAdapter(
    private var canteens: List<Canteen> = emptyList(),
    //表示接收一个“接收canteen类型参数”的函数 ，unit表示这个函数只是进行操作不返回返回值
    private val onItemClick: (Canteen) -> Unit = {}
) : RecyclerView.Adapter<CanteenListAdapter.CanteenViewHolder>() {

    class CanteenViewHolder(val binding: ItemCanteenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenViewHolder {
        val binding = ItemCanteenBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CanteenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CanteenViewHolder, position: Int) {
        val canteen = canteens[position]
        holder.binding.canteenNameTv.text = canteen.canteenName
        holder.binding.root.setOnClickListener { onItemClick(canteen) }
    }

    override fun getItemCount(): Int = canteens.size

    fun submitList(newCanteens: List<Canteen>) {
        canteens = newCanteens
        notifyDataSetChanged()
    }
}
