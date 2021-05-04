package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName

class ItemInfo(
    @SerializedName("id") val id : String,
    @SerializedName("article") val article : Article,



    )