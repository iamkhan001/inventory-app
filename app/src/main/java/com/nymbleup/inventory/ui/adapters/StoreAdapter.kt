package com.nymbleup.inventory.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.databinding.ItemStoreSelectBinding
import com.nymbleup.inventory.models.Outlet

class StoreAdapter(private val list: ArrayList<Outlet>, private val itemClickListener: OnItemClickListener<Outlet>): RecyclerView.Adapter<StoreAdapter.MyViewHolder>(), Filterable {

    private var filterList = list

    class MyViewHolder(itemView: ItemStoreSelectBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemStoreSelectBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemStoreSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val store = filterList[position]

        holder.binding.tvName.text = store.name

        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(store)
        }

    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().trim()

                filterList = if (charString.isEmpty()) {
                    list
                } else {

                    val filteredList = ArrayList<Outlet>()

                    for (row in list) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

                filterList = filterResults.values as ArrayList<Outlet>
                notifyDataSetChanged()

            }
        }
    }
}