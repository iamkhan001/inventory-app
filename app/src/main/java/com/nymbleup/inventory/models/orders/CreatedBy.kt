package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName

class CreatedBy(

    @SerializedName("email") val email : String,
    @SerializedName("first_name") val firstName : String,
    @SerializedName("last_name") val lastName : String,
    @SerializedName("phone_number") val phoneNumber : String

    )