package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class StoreTiming (
        @SerializedName("company") val company: String,
        @SerializedName("store") val store: String,
        @SerializedName("day") val day: Int,
        @SerializedName("opening_time") val openingTime: String,
        @SerializedName("closing_time") val closingTime: String,
        @SerializedName("closed") val closed: Boolean,
        @SerializedName("pre_opening_hour") val preOpeningHour: Int,
        @SerializedName("post_opening_hour") val postOpeningHour: Int,
)