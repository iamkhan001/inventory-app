package com.nymbleup.inventory.models

import com.google.gson.annotations.SerializedName

data class Function (
        @SerializedName("id") val id: Int,
        @SerializedName("function") val function: String,

) {
    @SerializedName("store_department") var storeDepartment: String = ""
    @SerializedName("hierarchy") var hierarchy: Int = 0
    @SerializedName("store_operations_hour") var storeOperationsHour: Int = 0
    @SerializedName("pre_opening_min_required") var preOpeningMinRequired: Int = 0
    @SerializedName("post_closing_min_required") var postClosingMinRequired: Int = 0
    @SerializedName("overlap_required") var overlapRequired: String = ""
    @SerializedName("planning_criteria") var planningCriteria: String? = ""
//        @SerializedName("interchangeable_functions") var interchangeableFunctions: ArrayList<Int>,

    constructor(id: Int, function: String, storeDepartment: String, hierarchy: Int,
                storeOperationsHour: Int, preOpeningMinRequired: Int, postClosingMinRequired: Int,
                overlapRequired: String, planningCriteria: String?
                ): this(id, function)
    {

        this.storeDepartment=storeDepartment
        this.hierarchy=hierarchy
        this.storeOperationsHour=storeOperationsHour
        this.preOpeningMinRequired=preOpeningMinRequired
        this.postClosingMinRequired=postClosingMinRequired
        this.overlapRequired=overlapRequired
        this.planningCriteria=planningCriteria
    }


}


