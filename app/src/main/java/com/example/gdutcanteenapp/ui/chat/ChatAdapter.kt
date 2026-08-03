package com.example.gdutcanteenapp.ui.chat

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gdutcanteenapp.R
import com.example.gdutcanteenapp.data.model.Msg

class ChatAdapter(val msgList: List<Msg>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LeftViewHolder(view: View):RecyclerView.ViewHolder(view){
        val leftMsg : TextView = view.findViewById(R.id.tv_left)
    }

    inner class RightViewHolder(view: View):RecyclerView.ViewHolder(view){
        val rightMsg : TextView = view.findViewById(R.id.tv_right)
    }

    fun getItemViewHolderType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Msg.TYPE_RECEIVED) {
        val view = View.inflate(parent.context, R.layout.item_message_l, null)
        LeftViewHolder(view)
    } else {
        val view = View.inflate(parent.context, R.layout.litem_message_r, null)
        RightViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder){
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            else -> throw IllegalArgumentException("Unknown ViewHolder type")
        }
    }

    override fun getItemCount() = msgList.size
}
