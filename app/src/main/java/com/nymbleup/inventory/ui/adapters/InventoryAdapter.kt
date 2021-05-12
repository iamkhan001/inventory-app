package com.nymbleup.inventory.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.ItemInventoryBinding
import com.nymbleup.inventory.models.Item

class InventoryAdapter(private val context: Context): RecyclerView.Adapter<InventoryAdapter.MyViewHolder>(), Filterable {

    private var list = ArrayList<Item>()
    private var filterList = ArrayList<Item>()

    fun setData(list: ArrayList<Item>){
        this.list = list
        filterList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: ItemInventoryBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemInventoryBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = filterList[position]
        item.init()

        val binding = holder.binding

        binding.tvItemName.text = item.article.name
        binding.tvDescription.text = "${item.article.code}\n1 pack = ${getIntVal(item.article.orderUnitConversion)}"

        binding.tvPackCount.text = item.quantity.toString()
        binding.tvLooseCount.text = item.looseQuantity.toString()

        binding.etPack.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                item.newPackQty = getIntVal(s.toString())
                list[position] = item
                updateQty(item.quantity, item.newPackQty, binding.tvPackStatus)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.etLoose.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                item.newLooseQty = getIntVal(s.toString())
                list[position] = item
                updateQty(item.looseQuantity, item.newLooseQty, binding.tvLooseStatus)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.imgPackAdd.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            item.newPackQty += 1
            list[position] = item
            binding.etPack.setText(item.newPackQty.toString())
            updateQty(item.quantity, item.newPackQty, binding.tvPackStatus)
        }

        binding.imgPackMinus.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            if (item.newPackQty == 0) {
                return@setOnClickListener
            }
            item.newPackQty -= 1
            list[position] = item
            binding.etPack.setText(item.newPackQty.toString())
            updateQty(item.quantity, item.newPackQty, binding.tvPackStatus)
        }

        binding.imgLooseAdd.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            item.newLooseQty += 1
            list[position] = item
            binding.etLoose.setText(item.newLooseQty.toString())
            updateQty(item.looseQuantity, item.newLooseQty, binding.tvLooseStatus)
        }

        binding.imgLooseMinus.setOnClickListener {
            if (disableClicks) {
                return@setOnClickListener
            }
            if (item.newLooseQty == 0) {
                return@setOnClickListener
            }
            item.newLooseQty -= 1
            list[position] = item
            binding.etLoose.setText(item.newLooseQty.toString())
            updateQty(item.looseQuantity, item.newLooseQty, binding.tvLooseStatus)
        }

        binding.etPack.setText(item.newPackQty.toString())
        binding.etLoose.setText(item.newLooseQty.toString())

        updateQty(item.quantity, item.newPackQty, binding.tvPackStatus)
        updateQty(item.looseQuantity, item.newLooseQty, binding.tvLooseStatus)

    }

    private fun updateQty(quantity: Int, newQty: Int, text: TextView) {

        val diff = newQty - quantity

        when{
            diff > 0 -> {
                text.text = diff.toString()
                text.setTextColor(ContextCompat.getColor(context, R.color.green))
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up,0)
            }
            diff < 0 -> {
                text.text = diff.toString()
                text.setTextColor(ContextCompat.getColor(context, R.color.red))
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down,0)
            }
            else -> {
                text.text = ""
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,0)
            }
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

                    val filteredList = ArrayList<Item>()

                    for (row in list) {
                        if (row.article.name.toLowerCase().contains(charString.toLowerCase())) {
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

                filterList = filterResults.values as ArrayList<Item>
                notifyDataSetChanged()

            }
        }
    }

    fun getData(): ArrayList<Item> = list

    var disableClicks = false

    companion object {

        fun getIntVal(value: String) : Int = try {
            value.toDouble().toInt()
        }catch (e: Exception) {
            0
        }

    }
}