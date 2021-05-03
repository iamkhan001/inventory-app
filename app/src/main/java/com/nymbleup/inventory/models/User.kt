package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class User(
            @SerializedName("first_name") val firstName: String?,
            @SerializedName("last_name") val lastName: String?,
            @SerializedName("mobile") val mobile: String?,
            @SerializedName("email") val email: String?,
        ){
        @SerializedName("id") var id: String? = ""
        @SerializedName("designation") var designation: String? = ""
        @SerializedName("phone_number") var phoneNumber: String? = ""
        @SerializedName("status") var status: String? = ""
        @SerializedName("access_level") var accessLevel: String? = ""
        @SerializedName("enabled") var enabled: Boolean? = true
        @SerializedName("employee_id") var employeeId: Int = 0
}

