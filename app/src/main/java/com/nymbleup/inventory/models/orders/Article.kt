package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName

class Article(
    @SerializedName("id") val id : String,
    @SerializedName("code") val code : Int,
    @SerializedName("name") val name : String,
    @SerializedName("primary_shelf_life") val primaryShelfLife : Int,


    )