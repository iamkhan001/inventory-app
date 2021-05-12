package com.nymbleup.workforce.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.ItemCalDateBinding
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.utils.MyDateTimeUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils.getDayOfMonth
import com.nymbleup.inventory.utils.MyDateTimeUtils.getMonthShortName
import com.nymbleup.inventory.utils.MyDateTimeUtils.isBetween

class CalendarDateWeekAdapter(private var weekSelectedStartDay: String, private val list: ArrayList<String>, private val events: ArrayList<String>, private val onDaySelectedListener: OnItemClickListener<String>): RecyclerView.Adapter<CalendarDateWeekAdapter.MyViewHolder>() {

    private lateinit var context: Context
    private val dateToday = MyDateTimeUtils.getDateToday()
    private var endDate = MyDateTimeUtils.getEndDate(weekSelectedStartDay)

    class MyViewHolder(itemView: ItemCalDateBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemCalDateBinding = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(ItemCalDateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val binding = holder.binding

        val date = list[position]


        if (date.isEmpty()) {
            binding.tvDay.text = ""
            binding.tvDay.setBackgroundResource(R.color.white)
            return
        }

        val day = getDayOfMonth(date)

        if (dateToday == date) {
            binding.viewSelected.visibility = View.VISIBLE
            binding.tvSelectedDay.text = day
            binding.tvMonth.text = getMonthShortName(date)
        }else {

            if (isBetween(weekSelectedStartDay, endDate, date)) {
                binding.tvDay.setBackgroundResource(R.color.cal_selected)
            }else {
                binding.tvDay.setBackgroundResource(R.color.white)
            }

            binding.viewSelected.visibility = View.GONE
            binding.tvDay.text = day
        }

        if (date in events) {
            binding.dot.visibility = View.VISIBLE
        }else {
            binding.dot.visibility = View.INVISIBLE
        }

        binding.root.setOnClickListener {
            onDaySelectedListener.onClick(date)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

}