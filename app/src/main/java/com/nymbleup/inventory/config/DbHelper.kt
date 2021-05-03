package com.nymbleup.inventory.config

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.text.GetChars
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nymbleup.inventory.config.*
import com.nymbleup.inventory.models.*
import com.nymbleup.inventory.models.Function
import com.nymbleup.inventory.utils.MyDateTimeUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils.checkBeforeAfter
import com.nymbleup.inventory.utils.MyDateTimeUtils.getDayOfWeek
import com.nymbleup.inventory.utils.MyDateTimeUtils.toLocalDate
import com.nymbleup.inventory.utils.MyDateTimeUtils.toLocalDateTime
import org.json.JSONArray
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class DbHelper(context: Context) {

    companion object {
        private const val TAG = "DbHelper"
    }

    private val database = AppDatabase(context)

    private fun isReadOpen(dbRead: SQLiteDatabase): SQLiteDatabase {
        if (dbRead.isOpen)
            return dbRead
        return database.readableDatabase
    }

    private fun isWriteOpen(dbWrite: SQLiteDatabase): SQLiteDatabase {
        if (dbWrite.isOpen)
            return dbWrite
        return database.writableDatabase
    }

    @Synchronized
    fun saveFunction(list: ArrayList<Function>) {

        val dbWrite = database.writableDatabase

        clearTable(dbWrite, TABLE_FUNCTIONS)

        for (item in list) {

            val data = ContentValues()
            data.put("id", item.id)
            data.put("function", item.function)
            data.put("store_department", item.storeDepartment)

            val h = if(item.hierarchy == 0) {
                100
            }else {
                item.hierarchy
            }
            data.put("hierarchy", h)

            data.put("store_operations_hour", item.storeOperationsHour)
            data.put("pre_opening_min_required", item.preOpeningMinRequired)
            data.put("post_closing_min_required", item.postClosingMinRequired)
            data.put("overlap_required", item.overlapRequired)
            data.put("planning_criteria", item.planningCriteria)

            try {
                dbWrite.insert(TABLE_FUNCTIONS, null, data)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dbWrite.close()
    }

    @Synchronized
    fun saveDepartments(list: ArrayList<Department>) {

        val dbWrite = database.writableDatabase

        clearTable(dbWrite, TABLE_DEPARTMENTS)

        for (item in list) {

            val data = ContentValues()
            data.put("id", item.id)
            data.put("department", item.department)

            try {
                dbWrite.insert(TABLE_DEPARTMENTS, null, data)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dbWrite.close()
    }

    @Synchronized
    fun saveOutlets(list: ArrayList<Outlet>) {

        val dbWrite = database.writableDatabase

        clearTable(dbWrite, TABLE_OUTLETS)

        for (item in list) {

            val active = if (item.active) {
                1
            }else {
                0
            }

            val setupComplete = if (item.isSetupComplete) {
                1
            }else {
                0
            }

            val data = ContentValues()
            data.put("id", item.id)
            data.put("store_id", item.storeId)
            data.put("name", item.name)
            data.put("country", item.country)
            data.put("region", item.region)
            data.put("sub_region", item.region)
            data.put("vendor_profiled", item.vendorProfile)
            data.put("website", item.website)
            data.put("phone", item.phone)
            data.put("email", item.email)
            data.put("type", item.type)
            data.put("store_type", item.id)
            data.put("address_line1", item.addressLine1)
            data.put("address_line2", item.addressLine2)
            data.put("city", item.city)
            data.put("pincode", item.pincode)
            data.put("latitude", item.latitude)
            data.put("longitude", item.longitude)
            data.put("active", active)
            data.put("setup_complete", setupComplete)
            data.put("ordering_begin_date", item.orderingBeginDate)

            try {
                dbWrite.insert(TABLE_OUTLETS, null, data)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dbWrite.close()
    }

    @Synchronized
    fun saveStoreTimings(list: ArrayList<StoreTiming>) {

        val dbWrite = database.writableDatabase

        clearTable(dbWrite, TABLE_STORE_TIMING)

        for (item in list) {

            val closed = if (item.closed) {
                1
            }else {
                0
            }

            val data = ContentValues()
            data.put("company", item.company)
            data.put("store", item.store)
            data.put("day", item.day)
            data.put("opening_time", item.openingTime)
            data.put("closing_time", item.closingTime)
            data.put("closed", closed)
            data.put("pre_opening_hour", item.preOpeningHour)
            data.put("post_opening_hour", item.postOpeningHour)

            try {
                dbWrite.insert(TABLE_STORE_TIMING, null, data)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dbWrite.close()
    }

    @Synchronized
    private fun clearTable(dbWrite: SQLiteDatabase, table: String) {
        dbWrite.execSQL("DELETE FROM $table")
    }

    @Synchronized
    fun getAllOutlets(): ArrayList<Outlet> {
        val list = ArrayList<Outlet>()

        val query = "SELECT * from $TABLE_OUTLETS as s ORDER BY name"
        val dbRead = database.readableDatabase

        val cursor = dbRead.rawQuery(query, null)
        while (cursor.moveToNext()) {

            val active = cursor.getInt(cursor.getColumnIndex("active"))
            val setupComplete = cursor.getInt(cursor.getColumnIndex("setup_complete"))
            val isActive = active == 1
            val isSetupComplete = setupComplete == 1

            val outlet = Outlet(
                id = cursor.getString(cursor.getColumnIndex("id")),
                storeId = cursor.getString(cursor.getColumnIndex("store_id")),
                name = cursor.getString(cursor.getColumnIndex("name")),
                country = cursor.getString(cursor.getColumnIndex("country")),
                region = cursor.getString(cursor.getColumnIndex("region")),
                subRegion = cursor.getString(cursor.getColumnIndex("sub_region")),
                vendorProfile = cursor.getString(cursor.getColumnIndex("vendor_profiled")),
                website = cursor.getString(cursor.getColumnIndex("website")),
                phone = cursor.getString(cursor.getColumnIndex("phone")),
                email = cursor.getString(cursor.getColumnIndex("email")),
                type = cursor.getString(cursor.getColumnIndex("type")),
                storeType = cursor.getString(cursor.getColumnIndex("store_type")),
                addressLine1 = cursor.getString(cursor.getColumnIndex("address_line1")),
                addressLine2 = cursor.getString(cursor.getColumnIndex("address_line2")),
                pincode = cursor.getString(cursor.getColumnIndex("pincode")),
                city = cursor.getString(cursor.getColumnIndex("city")),
                latitude = cursor.getString(cursor.getColumnIndex("latitude")),
                longitude = cursor.getString(cursor.getColumnIndex("longitude")),
                active = isActive,
                isSetupComplete = isSetupComplete,
                orderingBeginDate = cursor.getString(cursor.getColumnIndex("ordering_begin_date")),
            )

            list.add(outlet)
        }

        cursor.close()

        return list
    }


    @Synchronized
    fun getFunctionForEmployee() : Int {

        var functionId = 0

        val query = "SELECT  e.functions FROM employees as e WHERE e.id= $EMPLOYEE_ID"

        val dbRead = database.readableDatabase

        val cursor = dbRead.rawQuery(query, null)

        while (cursor.moveToNext()) {

            val functions: String? = cursor.getString(0)
            try {
                functions?.let {
                    val array = JSONArray(functions)
                    functionId = array.getInt(0)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }

        cursor.close()
        return functionId
    }

    @Synchronized
    fun getFunctionNameById(functionId: Int, dbRead: SQLiteDatabase): String {
        var name = ""

        val query = "SELECT function from functions where id = $functionId"

        val cursor = dbRead.rawQuery(query, null)

        if (cursor.moveToNext()) {
            name = cursor.getString(0)
        }

        cursor.close()

        return name
    }

    @Synchronized
    fun getFunctionNameById(functionId: Int?): String {

        if (functionId == null) {
            return ""
        }
        var name = ""

        val query = "SELECT function from functions where id = $functionId"

        val dbRead = database.readableDatabase

        val cursor = dbRead.rawQuery(query, null)

        if (cursor.moveToNext()) {
            name = cursor.getString(0)
        }

        cursor.close()
        dbRead.close()

        return name
    }

    @Synchronized
    fun clearData() {
        val dbWrite = database.writableDatabase
        clearTable(dbWrite, TABLE_DEPARTMENTS)
        clearTable(dbWrite, TABLE_FUNCTIONS)
        clearTable(dbWrite, TABLE_OUTLETS)
        clearTable(dbWrite, TABLE_STORE_TIMING)
        dbWrite.close()
    }

    @Synchronized
    fun loadFunctions(mFunctions: MutableLiveData<ArrayList<Function>>) {

        val list = ArrayList<Function>()

        val dbRead = database.readableDatabase

        try {

            val cursor = dbRead.rawQuery("select * from $TABLE_FUNCTIONS order by hierarchy, function", null)

            while (cursor.moveToNext()) {
                val function = Function(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("function")),
                    cursor.getString(cursor.getColumnIndex("store_department")),
                    cursor.getInt(cursor.getColumnIndex("hierarchy")),
                    cursor.getInt(cursor.getColumnIndex("store_operations_hour")),
                    cursor.getInt(cursor.getColumnIndex("pre_opening_min_required")),
                    cursor.getInt(cursor.getColumnIndex("post_closing_min_required")),
                    cursor.getString(cursor.getColumnIndex("overlap_required")),
                    cursor.getString(cursor.getColumnIndex("planning_criteria")),
                )

                list.add(function)
            }

            cursor.close()

        }catch (e: Exception) {
            e.printStackTrace()
        }finally {
            dbRead.close()
        }
        mFunctions.postValue(list)

    }

    fun getAllFunctions(): ArrayList<Function> {

        val list = ArrayList<Function>()

        val dbRead = database.readableDatabase

        try {

            val cursor = dbRead.rawQuery("select * from $TABLE_FUNCTIONS order by hierarchy, function", null)

            while (cursor.moveToNext()) {
                val function = Function(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("function")),
                        cursor.getString(cursor.getColumnIndex("store_department")),
                        cursor.getInt(cursor.getColumnIndex("hierarchy")),
                        cursor.getInt(cursor.getColumnIndex("store_operations_hour")),
                        cursor.getInt(cursor.getColumnIndex("pre_opening_min_required")),
                        cursor.getInt(cursor.getColumnIndex("post_closing_min_required")),
                        cursor.getString(cursor.getColumnIndex("overlap_required")),
                        cursor.getString(cursor.getColumnIndex("planning_criteria")),
                )

                list.add(function)
            }

            cursor.close()

        }catch (e: Exception) {
            e.printStackTrace()
        }finally {
            dbRead.close()
        }

        return list
    }

    @Synchronized
    fun loadDepartments(mFunctions: MutableLiveData<ArrayList<Department>>) {

        val list = ArrayList<Department>()

        val dbRead = database.readableDatabase

        try {

            val cursor = dbRead.rawQuery("select * from $TABLE_DEPARTMENTS order by department", null)

            while (cursor.moveToNext()) {
                val department = Department(
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("department")),
                )

                list.add(department)

            }

            cursor.close()

        }catch (e: Exception) {
            e.printStackTrace()
        }finally {
            dbRead.close()
        }

        mFunctions.postValue(list)

    }

    @Synchronized
    fun getAllDepartments(): ArrayList<Department> {

        val list = ArrayList<Department>()

        val dbRead = database.readableDatabase

        try {

            val cursor = dbRead.rawQuery("select * from $TABLE_DEPARTMENTS order by department", null)

            while (cursor.moveToNext()) {
                val department = Department(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("department")),
                )

                list.add(department)

            }

            cursor.close()

        }catch (e: Exception) {
            e.printStackTrace()
        }finally {
            dbRead.close()
        }

        return list
    }

    fun checkIsStoreOpen(date: String): Boolean {
        val dbRead = database.readableDatabase
        return isStoreOpenForDate(dbRead, date)
    }

    private fun isStoreOpenForDate(dbRead: SQLiteDatabase, date: String): Boolean {

        var openTime = ""
        var preOpenHour = 0.0

        val day = getDayOfWeek() +1
        val query = "SELECT opening_time, pre_opening_hour FROM store_timing WHERE day = $day;"
        Log.d(TAG,"isStoreOpenForDate $date > $query")
        val cursor = dbRead.rawQuery(query, null)
        if (cursor.moveToNext()) {
            openTime = cursor.getString(cursor.getColumnIndex("opening_time"))
            preOpenHour = cursor.getDouble(cursor.getColumnIndex("pre_opening_hour"))
        }
        cursor.close()

        if (openTime.isEmpty()) {
            return true
        }
        Log.e(TAG,"isStoreOpenForDate > openTime: $openTime, $preOpenHour preOpenHour ")

        val isOpen = MyDateTimeUtils.isStoreOpenFor(date, openTime, preOpenHour)

        Log.d(TAG,"isStoreOpenForDate $isOpen ")

        return isOpen
    }

}