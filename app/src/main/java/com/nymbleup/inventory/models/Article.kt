package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class Article (
        @SerializedName("id") val id: String,
        @SerializedName("name")  val name: String,
        @SerializedName("code")  val code: String,
        @SerializedName("type")  val type: String,
        @SerializedName("order_unit")  val orderUnit: String,
        @SerializedName("order_unit_conversion")  val orderUnitConversion: String,
        @SerializedName("base_unit")  val baseUnit: String,
        @SerializedName("default_cost")  val defaultCost: String,
)