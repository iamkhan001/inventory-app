package com.nymbleup.inventory.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nymbleup.inventory.databinding.ItemTextBinding
import com.nymbleup.inventory.models.Outlet

class OutletsAdapter(private var list: ArrayList<Outlet>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val vh: BusinessTypeViewHolder
        if (convertView == null) {
            val binding = ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            vh = BusinessTypeViewHolder(binding)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as BusinessTypeViewHolder
        }

        val data = list[position]
        vh.binding.tvName.text = data.name

        return vh.binding.root
    }

    override fun getItem(position: Int): Outlet {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class BusinessTypeViewHolder(itemView: ItemTextBinding) {
        val binding: ItemTextBinding = itemView
    }

}