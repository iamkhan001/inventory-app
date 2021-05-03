package com.nymbleup.inventory.config

import android.content.Context
import android.util.Log
import java.util.*

class AppDataProvider (context: Context) {

    companion object {
        private const val PREF = "user-data"

        private var ins: AppDataProvider? = null

        fun getInstance(context: Context): AppDataProvider {

            if (ins == null) {
                ins = AppDataProvider(context)
            }

            return ins!!
        }

    }

    private val preferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun setToken(token: String) {
        val editor = preferences.edit()
        editor.putString("token", token)

        val devId = preferences.getString("dev_id", "")

        if (devId.isNullOrEmpty()){
            val uniqueId = UUID.randomUUID().toString();
            editor.putString("dev_id", uniqueId)
            Log.e("Data", "New Dev ID Created")
        }

        editor.apply()
    }

    fun resetDevId() {
        val editor = preferences.edit()
        val uniqueId = UUID.randomUUID().toString();
        editor.putString("dev_id", uniqueId)
        editor.apply()
        Log.e("Data", "New Dev ID Created")

    }

    fun getToken(): String? = preferences.getString("token", "")
    fun getDevId(): String = preferences.getString("dev_id", "")!!

}