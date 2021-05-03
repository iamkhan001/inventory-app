package com.nymbleup.inventory.config

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val DB_NAME = "company"
private const val DB_VERSION = 1

const val TABLE_FUNCTIONS = "functions"
const val TABLE_DEPARTMENTS = "departments"
const val TABLE_OUTLETS = "outlets"
const val TABLE_STORE_TIMING = "store_timing"

private const val TAG = "database"

class AppDatabase(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createFunctionsTable(db)
        createDepartmentsTable(db)
        createOutletsTable(db)
        createStoreTimingTable(db)

    }

    private fun createStoreTimingTable(db: SQLiteDatabase) {

        val table = "CREATE TABLE '$TABLE_STORE_TIMING' (\n" +
                "'company' TEXT,\n" +
                "'store' TEXT,\n" +
                "'day' INTEGER,\n" +
                "'opening_time' TEXT,\n" +
                "'closing_time' TEXT,\n" +
                "'closed' INTEGER,\n" +
                "'pre_opening_hour' DOUBLE,\n" +
                "'post_opening_hour' DOUBLE\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createFunctionsTable(db: SQLiteDatabase) {

        val table = "CREATE TABLE '$TABLE_FUNCTIONS'  (\n" +
                "'id' INTEGER NOT NULL,\n" +
                "'function' TEXT DEFAULT '',\n" +
                "'store_department' TEXT,\n" +
                "'hierarchy' INTEGER,\n" +
                "'store_operations_hour' INTEGER,\n" +
                "'pre_opening_min_required' INTEGER,\n" +
                "'post_closing_min_required' INTEGER,\n" +
                "'overlap_required' TEXT,\n" +
                "'planning_criteria' TEXT\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createDepartmentsTable(db: SQLiteDatabase) {

        val table = "CREATE TABLE '$TABLE_DEPARTMENTS'  (\n" +
                "'id' TEXT NOT NULL,\n" +
                "'department' TEXT\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createScheduleTable(db: SQLiteDatabase, tableName: String) {

        val table = "CREATE TABLE IF NOT EXISTS '$tableName'  (\n" +
                "'id' TEXT NOT NULL,\n" +
                "'store' TEXT,\n" +
                "'partner' INTEGER,\n" +
                "'for_date' DATE,\n" +
                "'overtime' TEXT,\n" +
                "'extra_partner' INTEGER,\n" +
                "'auto_scheduled' BOOLEAN,\n" +
                "'start_time' DATETIME,\n" +
                "'end_time' DATETIME,\n" +
                "'fun_id' INTEGER,\n" +
                "'fun_name' TEXT,\n" +
                "'fun_store_department' TEXT,\n" +
                "'fun_hierarchy' INTEGER,\n" +
                "'fun_store_operations_hour' INTEGER,\n" +
                "'fun_pre_opening_min_required' INTEGER,\n" +
                "'fun_post_closing_min_required' INTEGER,\n" +
                "'fun_overlap_required' TEXT,\n" +
                "'fun_planning_criteria' TEXT,\n" +
                "'notes' TEXT,\n" +
                "'is_task_assigned' BOOLEAN\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createBreaksTable(db: SQLiteDatabase, tableName: String) {

        val table = "CREATE TABLE IF NOT EXISTS '$tableName'  (\n" +
                "'id' TEXT NOT NULL,\n" +
                "'break_start' DATETIME,\n" +
                "'break_end' DATETIME\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createOutletsTable(db: SQLiteDatabase) {

        val table = "CREATE TABLE IF NOT EXISTS '$TABLE_OUTLETS'  (\n" +
                "'id' TEXT NOT NULL,\n" +
                "'store_id' TEXT NULL,\n" +
                "'name' TEXT NOT NULL,\n" +
                "'country' TEXT NOT NULL,\n" +
                "'region' TEXT NOT NULL,\n" +
                "'sub_region' TEXT NOT NULL,\n" +
                "'vendor_profiled' TEXT NULL,\n" +
                "'website' TEXT NULL,\n" +
                "'phone' TEXT NULL,\n" +
                "'email' TEXT NULL,\n" +
                "'type' TEXT NULL,\n" +
                "'store_type' TEXT NULL,\n" +
                "'address_line1' TEXT NULL,\n" +
                "'address_line2' TEXT NULL,\n" +
                "'city' TEXT NULL,\n" +
                "'pincode' TEXT NULL,\n" +
                "'latitude' TEXT NULL,\n" +
                "'longitude' TEXT NULL,\n" +
                "'active' INTEGER NULL,\n" +
                "'setup_complete' INTEGER NULL,\n" +
                "'ordering_begin_date' TEXT NULL\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createTimingTable(db: SQLiteDatabase, tableName: String) {

        val table = "CREATE TABLE IF NOT EXISTS '$tableName' (\n" +
                "'timesheet_id' TEXT NOT NULL,\n" +
                "'cur_activity' TEXT NULL,\n" +
                "'employee_id' INTEGER NULL,\n" +
                "'is_approved' INTEGER,\n" +
                "'approved_by' INTEGER DEFAULT NULL,\n" +
                "'employee_full_name' TEXT NULL,\n" +
                "'employee_department' TEXT NULL,\n" +
                "'shift_date' DATE NOT NULL,\n" +
                "'outlet_id' TEXT NOT NULL,\n" +
                "'shift_start_id' TEXT NULL,\n" +
                "'shift_start_datetime' DATE NULL,\n" +
                "'shift_start_distance' TEXT NULL,\n" +
                "'shift_start_note' TEXT NULL,\n" +
                "'shift_start_lat' TEXT NULL,\n" +
                "'shift_start_long' TEXT NULL,\n" +
                "'shift_start_location' TEXT NULL,\n" +
                "'shift_end_id' TEXT NULL,\n" +
                "'shift_end_datetime' DATE NULL,\n" +
                "'shift_end_distance' TEXT NULL,\n" +
                "'shift_end_note' TEXT NULL,\n" +
                "'shift_end_lat' TEXT NULL,\n" +
                "'shift_end_long' TEXT NULL,\n" +
                "'shift_end_location' TEXT NULL,\n" +
                "'break1_start_id' TEXT NULL,\n" +
                "'break1_start_datetime' DATE NULL,\n" +
                "'break1_start_distance' TEXT NULL,\n" +
                "'break1_start_note' TEXT NULL,\n" +
                "'break1_start_lat' TEXT NULL,\n" +
                "'break1_start_long' TEXT NULL,\n" +
                "'break1_start_location' TEXT NULL,\n" +
                "'break1_end_id' TEXT NULL,\n" +
                "'break1_end_datetime' DATE NULL,\n" +
                "'break1_end_distance' TEXT NULL,\n" +
                "'break1_end_note' TEXT NULL,\n" +
                "'break1_end_lat' TEXT NULL,\n" +
                "'break1_end_long' TEXT NULL,\n" +
                "'break1_end_location' TEXT NULL,\n" +
                "'break2_start_id' TEXT NULL,\n" +
                "'break2_start_datetime' DATE NULL,\n" +
                "'break2_start_distance' TEXT NULL,\n" +
                "'break2_start_note' TEXT NULL,\n" +
                "'break2_start_lat' TEXT NULL,\n" +
                "'break2_start_long' TEXT NULL,\n" +
                "'break2_start_location' TEXT NULL,\n" +
                "'break2_end_id' TEXT NULL,\n" +
                "'break2_end_datetime' DATE NULL,\n" +
                "'break2_end_distance' TEXT NULL,\n" +
                "'break2_end_note' TEXT NULL,\n" +
                "'break2_end_lat' TEXT NULL,\n" +
                "'break2_end_long' TEXT NULL,\n" +
                "'break2_end_location' TEXT NULL\n" +
                ");"

        Log.d(TAG, table)

        try {
            db.execSQL(table)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

}