package com.nymbleup.inventory.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import java.util.*

object AddressUtils {

    fun getAddress(context: Context, location: Location): String {
        val addresses: List<Address>

        val geoCoder = Geocoder(context, Locale.getDefault())

        addresses = geoCoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
        )

        if (addresses.isNotEmpty()) {
            val address: String? = addresses[0].getAddressLine(0)
            val city: String? = addresses[0].locality
            val state: String? = addresses[0].adminArea
            val country: String? = addresses[0].countryName
            val postalCode: String? = addresses[0].postalCode
            val knownName: String? = addresses[0].featureName

            Log.e("address", "address: $address\nKnown Name: $knownName\nCity: $city\nState: $state\nCountry: $country\nPostal: $postalCode")

            if (address != null && knownName != null) {
                return "$address $knownName"
            }else if (address != null) {
                return address
            }else if (knownName != null) {
                return knownName
            }

        }


        return ""
    }

}