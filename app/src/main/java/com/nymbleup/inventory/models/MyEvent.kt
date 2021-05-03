package com.nymbleup.inventory.models

import android.location.Location

class MyEvent(val event: Event) {

    var location: Location? = null

    constructor(location: Location): this(Event.GET_LOCATION) {
        this.location = location
    }

    enum class Event{
        GET_LOCATION,
        GET_ADDRESS
    }

}