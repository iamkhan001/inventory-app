package com.nymbleup.inventory.models

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Item (
        @SerializedName("id") val id: String,
        @SerializedName("article") val articleId: String,
        @SerializedName("base_unit") val base_unit: String,
        @SerializedName("code") val code: String,
        @SerializedName("quantity") val quantity: Int,
        @SerializedName("loose_quantity") val looseQuantity: Int,
        @SerializedName("delivery_date") val deliveryDate: String,
        @SerializedName("expiry_date") val expiry_date: String,
        @SerializedName("unit_cost") val unitCost: String,
        @SerializedName("article_info") val article: Article
){
    var newPackQty = 0
    var newLooseQty = 0

    fun init() {
        Log.d("item", "item > ${article.name} > $quantity ? $newPackQty")
        if (newPackQty == 0) {
            newPackQty = quantity
        }
        if (newLooseQty == 0) {
            newLooseQty = looseQuantity
        }
    }

}