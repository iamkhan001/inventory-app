package com.nymbleup.inventory.repo

import androidx.annotation.MainThread

interface UIApiCallResponseListener {

    @MainThread
    fun onFailed(msg: String)

    @MainThread
    fun onSuccess(msg: String)

}