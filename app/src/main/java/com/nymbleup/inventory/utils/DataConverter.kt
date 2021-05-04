package com.nymbleup.inventory.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nymbleup.inventory.models.*
import com.nymbleup.inventory.models.Function
import com.nymbleup.inventory.models.orders.Order
import org.json.JSONArray

object DataConverter {

    fun toOutletList(obj: JSONArray): ArrayList<Outlet> {
        val typeToken = object : TypeToken<ArrayList<Outlet>>() {}.type
        return Gson().fromJson(obj.toString(), typeToken)
    }

    fun toCompanyInfo(obj: String): Company? {
        return Gson().fromJson(obj, Company::class.java)
    }

    fun toFunctionList(obj: JSONArray): ArrayList<Function> {
        val typeToken = object : TypeToken<ArrayList<Function>>() {}.type
        return Gson().fromJson(obj.toString(), typeToken)
    }

    fun toDepartmentList(obj: JSONArray): ArrayList<Department> {
        val typeToken = object : TypeToken<ArrayList<Department>>() {}.type
        return Gson().fromJson(obj.toString(), typeToken)
    }

    fun toStoreTimingList(obj: JSONArray): ArrayList<StoreTiming> {
        val typeToken = object : TypeToken<ArrayList<StoreTiming>>() {}.type
        return Gson().fromJson(obj.toString(), typeToken)
    }

    fun toInventory(data: JSONArray): ArrayList<Item> {
        val typeToken = object : TypeToken<ArrayList<Item>>() {}.type
        return Gson().fromJson(data.toString(), typeToken)
    }

    fun toOrders(data: JSONArray): ArrayList<Order> {
        val typeToken = object : TypeToken<ArrayList<Order>>() {}.type
        return Gson().fromJson(data.toString(), typeToken)
    }

}