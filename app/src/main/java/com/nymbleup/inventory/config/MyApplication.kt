package com.nymbleup.inventory.config

import android.app.Application
import com.google.firebase.FirebaseApp
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        val executorService: ExecutorService = Executors.newFixedThreadPool(8)
        FirebaseApp.initializeApp(applicationContext)
    }

}