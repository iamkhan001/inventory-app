package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName

class Vendor(

    @SerializedName ("id") val id:String,
    @SerializedName ("account") val account:Account,
    @SerializedName ("type") val type:String,



)