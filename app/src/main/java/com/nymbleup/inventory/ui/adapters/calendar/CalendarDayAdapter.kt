package com.nymbleup.workforce.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.databinding.ItemCalDayBinding

class CalendarDayAdapter(private val list: ArrayList<String>): RecyclerView.Adapter<CalendarDayAdapter.MyViewHolder>() {

    private lateinit var context: Context

    class MyViewHolder(itemView: ItemCalDayBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemCalDayBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(ItemCalDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val day = list[position]

        holder.binding.tvDay.text = day

    }

    override fun getItemCount(): Int {
        return list.size
    }

}