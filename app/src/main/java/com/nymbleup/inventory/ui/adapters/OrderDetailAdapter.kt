package com.nymbleup.inventory.ui.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.databinding.ItemOrderDetailBinding
import com.nymbleup.inventory.models.Item
import com.nymbleup.inventory.models.orders.Items

class OrderDetailAdapter: RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder>(), Filterable {

    private var list = ArrayList<Items>()
    private var filterList = ArrayList<Items>()

    fun setData(list: ArrayList<Items>) {
        this.list = list
        filterList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: ItemOrderDetailBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemOrderDetailBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = filterList[position]
        item.init()
        val binding = holder.binding

        binding.tvItemName.text = item.itemInfo.article.name
        binding.tvItemDescription.text = "${item.itemInfo.article.code}\n1 pack = ${item.itemInfo.article.primaryShelfLife} each"

        binding.etPack.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                item.newQty = InventoryAdapter.getIntVal(s.toString())
                list[position] = item
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.imgPackAdd.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            item.newQty += 1
            list[position] = item
            binding.etPack.setText(item.newQty.toString())
        }

        binding.imgPackMinus.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            if (item.newQty == 0) {
                return@setOnClickListener
            }
            item.newQty -= 1
            list[position] = item
            binding.etPack.setText(item.newQty.toString())
        }

        binding.etPack.setText(item.newQty.toString())


    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    var disableClicks = false

    fun getList(): ArrayList<Items> = list

}


