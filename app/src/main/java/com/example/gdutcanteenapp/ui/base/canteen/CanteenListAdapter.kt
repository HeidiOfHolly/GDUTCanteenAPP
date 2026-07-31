package com.example.gdutcanteenapp.ui.base.canteen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.R

class CanteenListAdapter():RecyclerView.Adapter<CanteenListAdapter.CanteenViewHolder>() {

    class CanteenViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val canteenName = view.findViewById(R.id.canteenNameTv) as? View
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_canteen, parent, false)
        return CanteenViewHolder(view)
    }

    override fun onBindViewHolder(holder: CanteenViewHolder, position: Int) {
        // 绑定数据到视图
        // holder.canteenName?.text = "Canteen Name" // 这里需要根据实际数据进行绑定
    }

    override fun getItemCount(): Int {

        return 0// 返回数据的数量，这里暂时返回0，需要根据实际数据进行修改
    }
}