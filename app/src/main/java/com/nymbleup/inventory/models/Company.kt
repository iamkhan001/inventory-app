package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

class Company {

    @SerializedName("name")
    var name: String = ""

    @SerializedName("nature_of_business")
    var natureOfBusiness: String = ""

    @SerializedName("membership")
    var membership: String = "trial"

    @SerializedName("country")
    var country: String = ""

    @SerializedName("state")
    var state: String = ""

    @SerializedName("city")
    var city: String = ""

    @SerializedName("address")
    var address: String = ""

    @SerializedName("zip_code")
    var zipCode: String = ""

    @SerializedName("financial_start")
    var financialStart: String? = null

    @SerializedName("is_single_location")
    var isSingleLocation = false

    @SerializedName("selected_sector")
    var selectedSector: String = ""

    @SerializedName("other_company_sector")
    var otherCompanySector: String = ""

    @SerializedName("more_store")
    var moreStore: String = ""

    @SerializedName("id")
    var id: String = ""
    @SerializedName("sector")
    var sector: String? = null
    @SerializedName("data_updated")
    var dataUpdated: Boolean = false
    @SerializedName("financial_end") val financialEnd: String? = null

}