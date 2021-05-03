package com.nymbleup.inventory.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
object MyDateTimeUtils {

    private const val TAG = "DAT"

    private val formatDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val shiftTimeFormatLocal = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    private val monthNames = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private val days = arrayOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    private const val startDayOfWeek = 2
    private const val endDayOfWeek = 0

    fun getDateToday(): String = dateFormat.format(Date())

    fun getDateYesterday(): String {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -1)
        return dateFormat.format(cal.time)
    }

    fun getDateTimeToday(): String = formatDateTime.format(Date())


    fun getDatePretty(): String {

        val objDateStart = Date()

        var startDateOfMonth = getDayOfMonth(objDateStart)
        val startDayName = getDayFullName(objDateStart)

        startDateOfMonth = when(startDateOfMonth) {
            "01", "11", "21", "31" -> "${startDateOfMonth}st"
            "02", "22" -> "${startDateOfMonth}nd"
            "03", "23" -> "${startDateOfMonth}rd"
            else -> "${startDateOfMonth}th"
        }

        val startMonthName = getMonthFullName(objDateStart)

        return "$startDayName, $startDateOfMonth $startMonthName"
    }

    fun getDateTime(dateString: String): String {
        val date = dateFormat.parse(dateString) ?: return ""
        val timeNow = timeFormat.format(Date())
        val time = timeFormat.parse(timeNow) ?: return ""
        val newDate = (date.time+time.time)
        return formatDateTime.format(newDate)
    }

    fun getDuration(start: String?, end: String?): String {

        if (start == null || end == null) {
            return ""
        }

        val startDate = try {
            formatDateTime.parse(start)
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }

        val endDate = try {
            formatDateTime.parse(end)
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (startDate == null || endDate == null) {
            return "00:00"
        }

        val diff = getDifference(endDate, startDate)
        val date = Date(diff)

        return getDuration(endDate, startDate)
    }

    private fun getDuration(date1: Date, date2: Date): String {
        val diff: Long = date1.time - date2.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        val h = if (hours < 10) {
            "0$hours"
        }else {
            "$hours"
        }
        val min = minutes % 60
        val m = if (min < 10) {
            "0$min"
        }else {
            "$min"
        }

        return if (days > 0) {
            "$days days, $h:$m"
        }else {
            "$h:$m"
        }
    }

    private fun hoursDifference(date1: Date, date2: Date): Long {
        return (date1.time - date2.time) / 1000
    }

    private fun getDifference(date1: Date, date2: Date): Long {
        return (date1.time - date2.time) / 10
    }

    fun getDefStartDate(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek != Calendar.MONDAY) {

            when(dayOfWeek) {
                1 -> calendar.add(Calendar.DATE, -6)
                3 -> calendar.add(Calendar.DATE, -1)
                4 -> calendar.add(Calendar.DATE, -2)
                5 -> calendar.add(Calendar.DATE, -3)
                6 -> calendar.add(Calendar.DATE, -4)
                7 -> calendar.add(Calendar.DATE, -5)
            }

        }

        return dateFormat.format(calendar.time)
    }

    fun getStartDate(startDate: String): String {
        val date = dateFormat.parse(startDate)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        Log.e("date before", "${Calendar.MONDAY} == ${calendar.get(Calendar.DAY_OF_WEEK)}")

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek != Calendar.MONDAY) {

            when(dayOfWeek) {
                1 -> calendar.add(Calendar.DATE, -6)
                3 -> calendar.add(Calendar.DATE, -1)
                4 -> calendar.add(Calendar.DATE, -2)
                5 -> calendar.add(Calendar.DATE, -3)
                6 -> calendar.add(Calendar.DATE, -4)
                7 -> calendar.add(Calendar.DATE, -5)
            }

        }

        Log.e("date after", "${Calendar.MONDAY} == ${calendar.get(Calendar.DAY_OF_WEEK)}")
        return dateFormat.format(calendar.time)
    }


    fun getEndDate(startDate: String): String {
        val date = dateFormat.parse(startDate)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.DATE, 6)
        return dateFormat.format(calendar.time)
    }

    fun getWeekDays(startDate: String): ArrayList<String> {
        val dates = ArrayList<String>()
        dates.add(startDate)

        val date = dateFormat.parse(startDate)

        val calendar = Calendar.getInstance()
        calendar.time = date!!

        for (i in 1..6) {
            calendar.add(Calendar.DATE, 1)
            dates.add(dateFormat.format(calendar.time))
        }

        return dates
    }

    private fun getDate(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, days)
        return  dateFormat.format(calendar.time)
    }

    private fun getDayOfWeek(date: Date): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c[Calendar.DAY_OF_WEEK]
    }

    private val dateOfMonthFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    private val dateMonthNameFormat = SimpleDateFormat("MMM", Locale.ENGLISH)

    private val dateMonthFullNameFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
    private val dateDayFullNameFormat = SimpleDateFormat("MMM", Locale.ENGLISH)

    private val dateMonthNameAndYearFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    private val dateYearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)

    fun setShowingDate(dateStart: String): String {

        val objDateStart = dateFormat.parse(dateStart) ?: return dateStart

        var startDateOfMonth = getDayOfMonth(objDateStart)
        val startDayName = getDayFullName(objDateStart)

        startDateOfMonth = when(startDateOfMonth) {
            "01", "11", "21", "31" -> "${startDateOfMonth}st"
            "02", "22" -> "${startDateOfMonth}nd"
            "03", "23" -> "${startDateOfMonth}rd"
            else -> "${startDateOfMonth}th"
        }

        val startMonthName = getMonthFullName(objDateStart)

        return "$startDayName, $startDateOfMonth $startMonthName"
    }

    fun setShowingDate(dateStart: String, dateEnd: String): String {

        val objDateStart = dateFormat.parse(dateStart)
        val objDateEnd = dateFormat.parse(dateEnd)

        if (objDateStart == null || objDateEnd == null) {
            return "$dateStart - $dateEnd"
        }

        var startDateOfMonth = getDayOfMonth(objDateStart)

        startDateOfMonth = when(startDateOfMonth) {
            "01", "11", "21", "31" -> "${startDateOfMonth}st"
            "02", "22" -> "${startDateOfMonth}nd"
            "03", "23" -> "${startDateOfMonth}rd"
            else -> "${startDateOfMonth}th"
        }

        var endDateOfMonth = getDayOfMonth(objDateEnd)

        endDateOfMonth = when(endDateOfMonth) {
            "01", "11", "21", "31" -> "${endDateOfMonth}st"
            "02", "22" -> "${endDateOfMonth}nd"
            "03", "23" -> "${endDateOfMonth}rd"
            else -> "${endDateOfMonth}th"
        }

        val startDayName = getDayShortName(objDateStart)
        val endDayName = getDayShortName(objDateEnd)

        val startMonthName = getMonthName(objDateStart)
        val endMonthName = getMonthName(objDateEnd)

        return "$startDayName, $startDateOfMonth $startMonthName - $endDayName, $endDateOfMonth $endMonthName"

    }

    fun formatDate(dateString: String): String {


        val objDateStart: Date
        try {
            objDateStart = dateFormat.parse(dateString) ?: return dateString
        }catch (e: Exception){
            return dateString
        }
        var startDateOfMonth = getDayOfMonth(objDateStart)
        val startDayName = getDayFullName(objDateStart)

        startDateOfMonth = when(startDateOfMonth) {
            "01", "11", "21", "31" -> "${startDateOfMonth}st"
            "02", "22" -> "${startDateOfMonth}nd"
            "03", "23" -> "${startDateOfMonth}rd"
            else -> "${startDateOfMonth}th"
        }

        val startMonthName = getMonthFullName(objDateStart)

        return "$startDayName, $startDateOfMonth $startMonthName"
    }

    fun getDayShortName(dateString: String): String {

        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)!!
    }

    fun getDayOfMonth(dateString: String): String {
        val date = dateFormat.parse(dateString)
        return dateOfMonthFormat.format(date!!)
    }

    fun getMonthShortName(dateString: String): String {
        val date = dateFormat.parse(dateString)
        return dateMonthNameFormat.format(date!!)
    }

    fun getMonthShortNameAndYear(dateString: String): String {
        val date = dateFormat.parse(dateString)
        return dateMonthNameAndYearFormat.format(date!!)
    }

    private fun getDayShortName(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)!!
    }

    private fun getDayOfMonth(date: Date): String {
        return dateOfMonthFormat.format(date)
    }

    private fun getMonthName(date: Date): String {
        return dateMonthNameFormat.format(date)
    }

    fun getMonthFullNameAndYear(dateString: String): String {
        val date = dateFormat.parse(dateString) ?: return dateString
        val cal = Calendar.getInstance()
        cal.time = date
        return "${monthNames[cal.get(Calendar.MONTH)]} ${dateYearFormat.format(date)}"
    }

    private fun getMonthFullName(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return monthNames[cal.get(Calendar.MONTH)]
    }

    private fun getDayFullName(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return days[cal.get(Calendar.DAY_OF_WEEK) - 1]
    }

    private val shiftTimeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    fun getTime(time: String?): String {
        if (time.isNullOrEmpty()) {
            return ""
        }
        try {
            if (time.isBlank()) {
                return ""
            }
            val date = formatDateTime.parse(time)
            date?.let {
                return shiftTimeFormat.format(date)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return "--"
    }

    fun getTimeDef(time: String?): String {
        if (time.isNullOrEmpty()) {
            return "-"
        }
        try {
            if (time.isBlank()) {
                return ""
            }
            val date = formatDateTime.parse(time)
            date?.let {
                return shiftTimeFormat.format(date)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return "-"
    }

    fun getSafeTime(time: String?): String {
        if (time.isNullOrEmpty()) {
            return ""
        }
        try {
            if (time.isBlank()) {
                return ""
            }
            val date = formatDateTime.parse(time)
            date?.let {
                return shiftTimeFormat.format(date)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun getMonth(dateString: String): Int {
        val date = dateFormat.parse(dateString) ?: return Calendar.JANUARY
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MONTH)
    }

    fun getAllDaysForMonth(dateString: String): ArrayList<String> {
        val date = dateFormat.parse(dateString)!!

        val days = ArrayList<String>()
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.DAY_OF_MONTH] = 1

        Log.e("Cal", "Diff ${cal.get(Calendar.DAY_OF_WEEK)} - ${Calendar.MONDAY}")

        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            addBlankDates(cal.get(Calendar.DAY_OF_WEEK), days)
        }

        val myMonth = cal[Calendar.MONTH]

        while (myMonth == cal[Calendar.MONTH]) {
            days.add(dateFormat.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        return days
    }

    fun getStartDateOfMonth(): String {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.DAY_OF_MONTH] = 1
        return dateFormat.format(cal.time)
    }

    fun getLastDateOfMonth(): String {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.DAY_OF_MONTH] = 1
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)

        return dateFormat.format(cal.time)
    }

    fun getAllDaysForPrevMonth(dateString: String): ArrayList<String> {
        val date = dateFormat.parse(dateString)!!
        val days = ArrayList<String>()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, -1)
        cal[Calendar.DAY_OF_MONTH] = 1

        Log.e("Cal", "Diff ${cal.get(Calendar.DAY_OF_WEEK)} - ${Calendar.MONDAY}")

        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            addBlankDates(cal.get(Calendar.DAY_OF_WEEK), days)
        }

        val myMonth = cal[Calendar.MONTH]

        while (myMonth == cal[Calendar.MONTH]) {
            days.add(dateFormat.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        return days
    }

    fun getAllDaysForNextMonth(dateString: String): ArrayList<String> {
        val date = dateFormat.parse(dateString)!!
        val days = ArrayList<String>()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, 1)
        cal[Calendar.DAY_OF_MONTH] = 1

        Log.e("Cal", "Diff ${cal.get(Calendar.DAY_OF_WEEK)} - ${Calendar.MONDAY}")

        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            addBlankDates(cal.get(Calendar.DAY_OF_WEEK), days)
        }

        val myMonth = cal[Calendar.MONTH]

        while (myMonth == cal[Calendar.MONTH]) {
            days.add(dateFormat.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        return days
    }

    private fun addBlankDates(dayOfWeek: Int, days: ArrayList<String>) {
        Log.e("cal", "week > $dayOfWeek ")
        when(dayOfWeek) {
            1 -> {
                days.add("")
                days.add("")
                days.add("")
                days.add("")
                days.add("")
                days.add("")
            }
            3 -> {
                days.add("")
            }
            4 -> {
                days.add("")
                days.add("")
            }
            5 -> {
                days.add("")
                days.add("")
                days.add("")
            }
            6 -> {
                days.add("")
                days.add("")
                days.add("")
                days.add("")
            }
            7 -> {
                days.add("")
                days.add("")
                days.add("")
                days.add("")
                days.add("")
            }
        }


    }

    fun getWeekDays(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("M")
        list.add("T")
        list.add("W")
        list.add("T")
        list.add("F")
        list.add("S")
        list.add("S")
        return  list
    }

    fun getHoursDiff(time: String, hours: Int, min: Int): String {
        try {
            val date = formatDateTime.parse(time) ?: return ""
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.HOUR_OF_DAY, hours)
            cal.add(Calendar.MINUTE, min)
            return formatDateTime.format(cal.time)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getNextMonthDate(month: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, month)
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        return dateFormat.format(cal.time)
    }

    fun getLastMonthDate(month: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, month)
        cal[Calendar.DAY_OF_MONTH] = 1
        return dateFormat.format(cal.time)
    }

    fun isBetween(startDate: String, endDate: String, curDate: String): Boolean {

        try {
            if (startDate.isEmpty() || endDate.isEmpty() || curDate.isEmpty()) {
                return false
            }
            val date1 = dateFormat.parse(startDate)!!
            val date2 = dateFormat.parse(endDate)!!
            val date = dateFormat.parse(curDate)!!

            return date in date1..date2

        }catch (e: Exception) {
        }

        return false
    }

    fun isBetweenTime(startDate: String, endDate: String, curDate: String): Boolean {

        try {

            val date1 = formatDateTime.parse(startDate)!!
            val date2 = formatDateTime.parse(endDate)!!
            val date = formatDateTime.parse(curDate)!!

            return date in date1..date2

        }catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun getAddDate(dateString: String, days: Int): String {
        val date = dateFormat.parse(dateString)!!
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, days)
        return dateFormat.format(cal.time)
    }

    private val formatDateTimeUtc = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormatUtc = SimpleDateFormat("yyyy-MM-dd")

    fun toLocalDate(datesToConvert: String?): String? {
        try {
            if (datesToConvert == null) {
                return null
            }
            dateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")
            val value = dateFormatUtc.parse(datesToConvert)!!
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            dateFormat.timeZone = timeZone
            return dateFormat.format(value)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun toLocalDateTime(datesToConvert: String?): String? {
        try {
            if (datesToConvert == null) {
                return null
            }
            formatDateTimeUtc.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatDateTimeUtc.parse(datesToConvert)!!
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            formatDateTimeUtc.timeZone = timeZone
            return formatDateTimeUtc.format(value)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun toLocalTime(datesToConvert: String?): String? {
        try {
            if (datesToConvert == null) {
                return ""
            }
            formatDateTimeUtc.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatDateTimeUtc.parse(datesToConvert)!!
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            shiftTimeFormatLocal.timeZone = timeZone
            return shiftTimeFormatLocal.format(value)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getTimeInSeconds(start: String): Long {
        try {
            val d1 = formatDateTime.parse(start)!!
            return d1.time
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun getDiffInSecondsFromNow(start: String): Int {
        try {
            Log.d(TAG, "getDiffInSecondsFromNow> $start")
            val d1 = formatDateTime.parse(start)!!
            val d2 = Date()

            Log.d(TAG, "getDiffInSecondsFromNow> ${d2.time} - ${d1.time}")

            return ((d2.time - d1.time) / 1000).toInt()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun getDiffInSeconds(start: String, end: String): Int {
        try {
            val d1 = formatDateTime.parse(start)!!
            val d2 = formatDateTime.parse(end)!!
            return ((d2.time - d1.time) / 1000).toInt()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

    fun getTimeDiffInSeconds(start: String, end: String): Int {
        try {
            val d1 = timeFormat.parse(start)!!
            val d2 = timeFormat.parse(end)!!
            return ((d2.time - d1.time) / 1000).toInt()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun checkBeforeAfter(dateToCompare: String): Int {
        val today = Date()
        val date = dateFormat.parse(dateToCompare)!!
        if (today.time == date.time) {
            return 0
        }
        return when {
            today < date -> 1
            today > date -> -1
            else -> 0
        }
    }

    fun getWorkingTime(
        timeStart: String?,
        timeEnd: String?,
        shiftStartDatetime: String?,
        shiftEndDatetime: String?
    ): String {

        if (timeStart == null || timeEnd == null || shiftStartDatetime == null || shiftEndDatetime == null) {
            return ""
        }

        val duration = getDiffInSeconds(timeStart, timeEnd)
        val actualDuration = getDiffInSeconds(shiftStartDatetime, shiftEndDatetime)

        val totalSecs = actualDuration - duration

        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60

        val h = if (hours < 0) {
            -hours
        }else {
            hours
        }

        val m = if (minutes < 0) {
            -minutes
        }else {
            minutes
        }

        val timeString = String.format("%02d Hrs, %02d Mins", h, m)

        return if (actualDuration < duration) {
            "Shift short by $timeString"
        }else {
            "Shift exceeded by $timeString"
        }

    }

    fun getTime2D(time: Int): String = if (time>9) {
        "$time"
    }else {
        "0$time"
    }

    fun isLessThanToday(dateEnd: String): Boolean {
        val date = dateFormat.parse(dateEnd)!!
        val today = Date()
        return date.time < today.time
    }

    @SuppressLint("SimpleDateFormat")
    fun toUtcFormat(time: String?): String {
        try {
            if (time == null) {
                return ""
            }
            val date = formatDateTimeUtc.parse(time)!!
            Log.e("Date", "Local > $date ")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat.format(date)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun secsToTime(seconds: Int): String {

        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val secs = seconds % 60

        val h = if (hours > 9) {
            "$hours"
        }else {
            "0$hours"
        }

        val m = if (minutes > 9) {
            "$minutes"
        }else {
            "0$minutes"
        }

        val s = if (secs > 9) {
            "$secs"
        }else {
            "0$secs"
        }

        return if (hours == 0) {
            "$m:$s Mins"
        }else {
            "$h:$m:$s Hrs"
        }
    }

    private val durationFormat = SimpleDateFormat("HH:mm:ss")

    fun getShiftDurationInSec(duration: String?): Int {

        if (duration == null) {
            return -1
        }
        Log.d("time", "duration $duration")
        return try {
            val time = durationFormat.parse(duration)!!
            (time.time).toInt() / 100
        }catch (e: Exception) {
            e.printStackTrace()
            -1
        }

    }

    fun getShiftDurationInMilliSec(duration: String?): Long {

        if (duration == null) {
            return -1
        }

        return try {
           durationFormat.parse(duration)!!.time
        }catch (e: Exception) {
            e.printStackTrace()
            -1
        }

    }

    fun getShiftEndTime(dateString: String, duration: String?): String {

        Log.e("getShiftEndTime", "dateString: $dateString > $duration")
        if (duration.isNullOrEmpty()) {
            return ""
        }

        var dateTime = durationFormat.parse(duration)!!.time * 10

        if (dateTime < 0) {
            dateTime *= -1
        }

        val date = formatDateTime.parse(dateString)!!
        val timeStamp = date.time+dateTime

        val nDate = Date(timeStamp)

        Log.e(
            "getShiftEndTime", "time > ${date.time} + duration $dateTime > ${
                formatDateTime.format(
                    nDate
                )
            }"
        )

        return formatDateTime.format(nDate)
    }

    fun getDateObject(storeOpeningTime: String): Date? {
        return try {
            formatDateTime.parse(storeOpeningTime)
        }catch (e: Exception) {
            null
        }
    }

    fun isStoreOpenFor(dateFor: String, time: String, preOpeningHour: Double): Boolean {
        val dateString = "$dateFor $time"

        Log.e(TAG, "isStoreOpenFor > $dateString")

        val date = formatDateTime.parse(dateString) ?: return true

        val min = preOpeningHour * 60
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, -min.toInt())

        return calendar.time.time < Date().time
    }

    fun getDayOfWeek(): Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    fun getDiffInDays(start: String, end: String): Long {
        try {
            if (start == "" || end == "") {
                return 0
            }
            val d1 = dateFormat.parse(start)!!
            val d2 = dateFormat.parse(end)!!
            val diff: Long = d2.time - d1.time
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return 1
    }

    fun isDateBefore(start: String, end: String): Boolean {
        try {
            if (start == "" || end == "") {
                return true
            }
            val d1 = dateFormat.parse(start)!!
            val d2 = dateFormat.parse(end)!!
            return d2.time < d1.time
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

}