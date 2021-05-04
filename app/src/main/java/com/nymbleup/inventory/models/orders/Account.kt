package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName

class Account(

    @SerializedName ("id") val id :String,
    @SerializedName ("name") val name :String,
    @SerializedName ("type") val type :String


)