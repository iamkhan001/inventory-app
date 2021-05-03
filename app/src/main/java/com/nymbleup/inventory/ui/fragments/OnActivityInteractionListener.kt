package com.nymbleup.inventory.ui.fragments

interface OnActivityInteractionListener {

    fun askLocationPermission(): Boolean

    fun getLocation()

}