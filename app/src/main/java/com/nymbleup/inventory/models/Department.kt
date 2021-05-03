package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class Department(
        @SerializedName("id") val id: String,
        @SerializedName("department") val department: String,
)