package com.nymbleup.inventory.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.DialogCalendarBinding
import com.nymbleup.inventory.models.ScheduleView
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.utils.MyDateTimeUtils

import com.nymbleup.workforce.ui.adapters.CalendarDateDayAdapter
import com.nymbleup.workforce.ui.adapters.CalendarDateWeekAdapter
import com.nymbleup.workforce.ui.adapters.CalendarDayAdapter

import java.util.*
import kotlin.collections.ArrayList

class CalendarDialog(private val viewType: ScheduleView, private val dateStart: String, private val onDatesSelectedListener: OnDateRangeSelectedListener): DialogFragment() {

    companion object {

        private val TAG = "CalendarDialog"

        private var calendarDialog: CalendarDialog? = null

        fun show(viewType: ScheduleView, dateStart: String, fragmentManager: FragmentManager, onDatesSelectedListener: OnDateRangeSelectedListener) {
            hide()
            calendarDialog = CalendarDialog(viewType, dateStart, onDatesSelectedListener)
            calendarDialog?.show(fragmentManager, TAG)
        }

        fun hide() {
            calendarDialog?.dismiss()
        }

    }

    private val onDateSelected = object : OnItemClickListener<String> {
        override fun onClick(item: String) {

            val startDate: String
            val endDate: String

            if (viewType == ScheduleView.WEEK) {
                startDate = MyDateTimeUtils.getStartDate(item)
                endDate = MyDateTimeUtils.getEndDate(startDate)
                onDatesSelectedListener.onRangeSelected(startDate, endDate)
            }else {
                onDatesSelectedListener.onDateSelected(item)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 200)
        }
    }


    private var safebinding: DialogCalendarBinding? = null
    private val binding get() = safebinding!!
    private var month = Calendar.JANUARY
    private var dates = ArrayList<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window: Window? = dialog!!.window

        window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)

        val params: WindowManager.LayoutParams? = window?.attributes
        params?.y = 100
        window?.attributes = params

        safebinding = DialogCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val width = resources.getDimensionPixelSize(R.dimen.cal_w)
        val height = resources.getDimensionPixelSize(R.dimen.cal_h)
        dialog?.window?.setLayout(width, height)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val days = MyDateTimeUtils.getWeekDays()

        val adapter = CalendarDayAdapter(days)
        binding.rvDays.adapter = adapter

//        val dateEnd = scheduleViewModel.dateEnd
        dates = MyDateTimeUtils.getAllDaysForMonth(dateStart)
        showMonthDates(dates)

        Log.e(TAG,"def month $month")

        binding.btnNext.setOnClickListener {
            dates = MyDateTimeUtils.getAllDaysForNextMonth(dates[dates.size - 1])
            showMonthDates(dates)
        }

        binding.btnPrev.setOnClickListener {
            Log.e(TAG,"month $month")
            var date = ""
            for (d in dates) {
                if (d == "") {
                    continue
                }
                date = d
                break
            }
            dates = MyDateTimeUtils.getAllDaysForPrevMonth(date)
            showMonthDates(dates)
        }

    }

    private fun showMonthDates(dates: ArrayList<String>) {

        Log.e(TAG,"last day ${dates[dates.size-1]}")
        binding.tvMonth.text = MyDateTimeUtils.getMonthFullNameAndYear(dates[dates.size-1])

        if (viewType == ScheduleView.WEEK) {
            val weekAdapter = CalendarDateWeekAdapter(dateStart, dates, ArrayList(), onDateSelected)
            binding.rvDates.adapter = weekAdapter
            return
        }

        val dayAdapter = CalendarDateDayAdapter(dateStart, dates, ArrayList(), onDateSelected)
        binding.rvDates.adapter = dayAdapter

    }

    interface OnDateRangeSelectedListener {

        fun onRangeSelected(dateStart: String, dateEnd: String)

        fun onDateSelected(date: String)

    }

}