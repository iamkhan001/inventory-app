package com.nymbleup.inventory.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.ItemOrderListBinding
import com.nymbleup.inventory.models.orders.Order
import com.nymbleup.inventory.utils.MyDateTimeUtils

class OrderListAdapter(private val onItemClickListener: OnItemClickListener<Order>):RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {

    var list = ArrayList<Order>()

    companion object {

        fun getOrderStatusName(status: String): String =  when(status){
            "cancelled" -> {
               "Cancelled"
            }
            "recieved_closed" -> {
                "Closed"
            }
            "dispatched" ->{
                "Dispatched"
            }
            else -> {
                status
            }
        }

    }

    fun setData(list:ArrayList<Order>){
        this.list = list

        notifyDataSetChanged()
    }

    class MyViewHolder (val binding: ItemOrderListBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemOrderListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val order  =list[position]

        val html = "<b>${order.code}</b> on <b>${MyDateTimeUtils.formatDate(order.createdOn)}</b> for <b>${order.outletInfo.name}</b> was sent to <b>${order.vendor.account.name}</b> INR <b>${order.totalAmount}</b>"

        holder.binding.tvPoAddress.text=  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
            holder.binding.tvCreaterName.text= "Created by: ${order.createdBy.firstName} ${order.createdBy.lastName}"
        holder.binding.tvDateAndTime.text= MyDateTimeUtils.formatDate(order.dateEstimated)
        holder.binding.btnRecAndCan.setBackgroundResource(R.drawable.bg_box_green)

        Log.d("order","status > ${order.status}")

        when(order.status){
            "cancelled" -> {
                holder.binding.btnRecAndCan.setBackgroundResource(R.drawable.bg_box_red)
            }
            "recieved_closed" -> {
                holder.binding.btnRecAndCan.text = "Closed"
            }

            "dispatched" ->{
                holder.binding.btnRecAndCan.text="Dispatched"
            }

            else -> {
                holder.binding.btnRecAndCan.text = order.status
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(order)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



}