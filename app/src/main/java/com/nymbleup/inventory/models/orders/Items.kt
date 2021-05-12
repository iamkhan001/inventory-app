package com.nymbleup.inventory.models.orders

import android.util.Log
import com.google.gson.annotations.SerializedName

class Items(
    @SerializedName ("id") val id : String,
    @SerializedName ("order") val order : String,
    @SerializedName ("quantity") val quantity : Int,
    @SerializedName ("unit") val unit : String,
    @SerializedName ("unit_conversion") val unitConversion : String,
    @SerializedName ("amount") val amount : Double,
    @SerializedName ("item_info") val itemInfo : ItemInfo,
    @SerializedName ("is_return") val isReturn : Boolean,
    @SerializedName ("return_reason") val returnReason : String,
    @SerializedName ("expiry_date") val expiryDate : String,
    @SerializedName ("batch_number") val batchNumber : String,



){
    var newQty = 0

    fun init() {
        Log.d("item", "item > ${itemInfo.article.name} > $quantity ? $newQty")
        if (newQty == 0) {
            newQty = quantity
        }
    }

}