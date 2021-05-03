package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class Outlet(
        @SerializedName("id") val id: String,
        @SerializedName("store_id") val storeId: String?,
        @SerializedName("name") val name: String,
        @SerializedName("country") val country: String,
        @SerializedName("region") val region: String,
        @SerializedName("subregion") val subRegion: String,
        @SerializedName("vendor_profiled") val vendorProfile: String?,
        @SerializedName("website") val website: String?,
        @SerializedName("phone") val phone: String,
        @SerializedName("email") val email: String,
        @SerializedName("type") val type: String,
        @SerializedName("store_type") val storeType: String,
        @SerializedName("address_line1") val addressLine1: String,
        @SerializedName("address_line2") val addressLine2: String,
        @SerializedName("city") val city: String,
        @SerializedName("pincode") val pincode: String,
        @SerializedName("latitude") val latitude: String?,
        @SerializedName("longitude") val longitude: String?,
        @SerializedName("active") val active: Boolean,
        @SerializedName("is_setup_complete") val isSetupComplete: Boolean,
        @SerializedName("ordering_begin_date") val orderingBeginDate: String,
)

