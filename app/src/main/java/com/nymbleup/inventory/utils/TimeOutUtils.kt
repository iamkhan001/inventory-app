package com.nymbleup.inventory.utils

import android.os.Handler
import android.os.Looper
import android.util.Log

class TimeOutUtils(private val timeoutListener: OnTimeoutListener) {


    private val disconnectHandler = Handler(Looper.getMainLooper()) {
        true
    }

    private val disconnectCallback = Runnable {
        timeoutListener.onTimeout()
    }

    fun resetDisconnectTimer(timeoutInSec: Int) {
        val timeout = (timeoutInSec * 1000).toLong()

        Log.e("timeout","Reset timeout: $timeout")

        disconnectHandler.removeCallbacks(disconnectCallback)
        disconnectHandler.postDelayed(disconnectCallback, timeout)
    }

    fun stopDisconnectTimer() {
        Log.e("timeout","Stop Disconnect Timer")
        disconnectHandler.removeCallbacks(disconnectCallback)
    }

    interface OnTimeoutListener {

        fun onTimeout()

    }


}