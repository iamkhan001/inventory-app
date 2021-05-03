package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class Store(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
)