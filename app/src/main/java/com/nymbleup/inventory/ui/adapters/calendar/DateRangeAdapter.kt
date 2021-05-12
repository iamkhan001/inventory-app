package com.nymbleup.inventory.ui.adapters.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.ItemScheduleWeekDayBinding
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.utils.MyDateTimeUtils


class DateRangeAdapter :  RecyclerView.Adapter<DateRangeAdapter.MyViewHolder>() {

    var onDayClickListener: OnItemClickListener<String>? = null
    private var list = ArrayList<String>()
    private val dateToday = MyDateTimeUtils.getDateToday()
    private var selected = ""
    private lateinit var context: Context

    fun setDataList(list: ArrayList<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun selected(selected: String) {
        this.selected = selected
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: ItemScheduleWeekDayBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemScheduleWeekDayBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(ItemScheduleWeekDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.e("date","selected $selected")
        val date = list[position]

        val dayName = MyDateTimeUtils.getDayShortName(date)
        val dateOfMonth = MyDateTimeUtils.getDayOfMonth(date)

        holder.binding.tvDayName.text = dayName
        holder.binding.tvDayDate.text = dateOfMonth

        val background: Int
        val text: Int
        if (date == dateToday) {
            background = ContextCompat.getColor(context, R.color.white)
            text = ContextCompat.getColor(context, R.color.black)
        }else {

            if (selected == date) {
                background = ContextCompat.getColor(context, R.color.dal_day_selected)
                text = ContextCompat.getColor(context, R.color.textPrimary)
            }else {
                background = ContextCompat.getColor(context, R.color.primary)
                text = ContextCompat.getColor(context, R.color.white)
            }


        }

        holder.binding.viewDay.setBackgroundColor(background)
        holder.binding.tvDayDate.setTextColor(text)
        holder.binding.tvDayName.setTextColor(text)

        onDayClickListener?.let {
            holder.binding.root.setOnClickListener {
                selected(date)
                onDayClickListener?.onClick(date)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}