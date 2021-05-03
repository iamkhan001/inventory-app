package com.nymbleup.inventory.config

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.nymbleup.inventory.models.Company
import com.nymbleup.inventory.models.Outlet

class StoreDataProvider(context: Context) {

    companion object {

        fun getInstance(context: Context): StoreDataProvider {

            var storeDataProvider: StoreDataProvider? = null

            if (storeDataProvider == null) {
                storeDataProvider = StoreDataProvider(context)
            }

            return storeDataProvider
        }

    }

    private val preferences = context.getSharedPreferences("store", Context.MODE_PRIVATE)

    fun saveStore(outlet: Outlet) {
        Log.e("StoreData","Save Outlet ${outlet.id}")

        preferences.edit().apply {
            putString("s_id", outlet.id)
            putString("s_store_id", outlet.storeId)
            putString("s_name", outlet.name)
            putString("s_country", outlet.country)
            putString("s_region", outlet.region)
            putString("s_subregion", outlet.subRegion)
            putString("s_website", outlet.website)
            putString("s_phone", outlet.phone)
            putString("s_email", outlet.email)
            putString("s_type", outlet.type)
            putString("s_store_type", outlet.storeType)
            putString("s_address_line1", outlet.addressLine1)
            putString("s_address_line2", outlet.addressLine2)
            putString("s_city", outlet.city)
            putString("s_pincode", outlet.pincode)
            putString("s_latitude", outlet.latitude)
            putString("s_longitude", outlet.longitude)
            putString("s_ordering_begin_date", outlet.orderingBeginDate)
            putBoolean("s_active", outlet.active)
            putBoolean("s_is_setup_complete", outlet.isSetupComplete)
            apply()
        }
    }

    fun getStore(): Outlet {
        return Outlet(
                    preferences.getString("s_id", "")!!,
                    preferences.getString("s_store_id", "")!!,
                    preferences.getString("s_name", "")!!,
                    preferences.getString("s_country", "")!!,
                    preferences.getString("s_region", "")!!,
                    preferences.getString("s_subregion", "")!!,
                    preferences.getString("s_website", ""),
                    preferences.getString("s_website", ""),
                    preferences.getString("s_phone", "")!!,
                    preferences.getString("s_email", "")!!,
                    preferences.getString("s_type", "")!!,
                    preferences.getString("s_store_type", "")!!,
                    preferences.getString("s_address_line1", "")!!,
                    preferences.getString("s_address_line2", "")!!,
                    preferences.getString("s_city", "")!!,
                    preferences.getString("s_pincode", "")!!,
                    preferences.getString("s_latitude", ""),
                    preferences.getString("s_longitude",""),
                    preferences.getBoolean("s_active", false),
                    preferences.getBoolean("s_is_setup_complete",false),
                    preferences.getString("s_ordering_begin_date","")!!,
                )
    }

    fun getStoreId(): String = preferences.getString("s_id", "")!!

    fun logout() {
        preferences.edit().apply {
            clear()
            apply()
        }

    }

    fun getStoreLat(): String? = preferences.getString("s_latitude", "")
    fun getStoreLon(): String? = preferences.getString("s_longitude", "")
    fun getStoreName(): String? = preferences.getString("s_name", "")

    fun saveCompanyInfo(company: Company?) {

        if (company == null) {
            return
        }

        preferences.edit {
            putString("companyId", company.id)
            putString("companyName", company.name)
            putString("companyMembership", company.membership)
            putString("companyCountry", company.country)
            putString("companyAddress", company.address)
            putString("companyZipCode", company.zipCode)
            putBoolean("companyIsSingleLocation", company.isSingleLocation)
            putBoolean("companyDataUpdated", company.dataUpdated)
            putString("companySector", company.sector)
            putString("companyFinancialStart", company.financialStart)
            putString("companyFinancialEnd", company.financialEnd)
        }
    }

    fun getCompanyInfo(): Company {
        val company = Company()

        company.id = preferences.getString("companyId", "")!!
        company.name = preferences.getString("companyName", "")!!
        company.membership = preferences.getString("companyMembership", "")!!
        company.country = preferences.getString("companyCountry", "")!!
        company.address = preferences.getString("companyAddress", "")!!
        company.zipCode = preferences.getString("companyZipCode", "")!!
        company.sector = preferences.getString("companySector", "")
        company.financialStart = preferences.getString("companyFinancialStart", "")
        company.financialStart = preferences.getString("companyFinancialStart", "")
        company.dataUpdated = preferences.getBoolean("companyDataUpdated", false)
        company.isSingleLocation = preferences.getBoolean("companyIsSingleLocation", false)

        return company
    }

    fun getCompanyName(): String? =  preferences.getString("companyName", "")

    fun getOpeningTime(): String = preferences.getString("storeOpeningTime", "")!!
    fun getClosingTime(): String = preferences.getString("storeClosingTime", "")!!
    fun saveApplicableToAll(applicableToAll: Boolean) {
        preferences.edit().apply {
            putBoolean("applicableToAll", applicableToAll)
            apply()
        }
    }

}