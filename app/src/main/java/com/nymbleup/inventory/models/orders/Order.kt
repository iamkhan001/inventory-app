package com.nymbleup.inventory.models.orders

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order
    (

    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("outlet") val outlet: String,
    @SerializedName("vendor") val vendor: Vendor,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("special_instruction") val specialInstruction: String,
    @SerializedName("delivery_time_from") val deliveryTimeFrom: String,
    @SerializedName("delivery_time_to") val deliveryTimeTo: String,
    @SerializedName("date_estimated") val dateEstimated: String,
    @SerializedName("date_delivered") val dateDelivered: String,
    @SerializedName("tax") val tax: Double,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("created_on") val createdOn: String,
    @SerializedName("last_change_on") val lastChangeOn: String,
    @SerializedName("items") val items: ArrayList<Items>,
    @SerializedName("outlet_info") val outletInfo: OutletInfo,
    @SerializedName("comments") val comments: String,
    @SerializedName("created_by") val createdBy: CreatedBy

    ): Serializable{


    }