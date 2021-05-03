package com.nymbleup.inventory.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.nymbleup.inventory.R
import com.nymbleup.inventory.config.UserDataProvider
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.MyMessage

class SplashActivity : AppCompatActivity() {

    private lateinit var context: Context

    private val dataViewModel: SupportDataViewModel by viewModels()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        context = this


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        try {
            window.statusBarColor = ContextCompat.getColor(context, R.color.primary)
        }catch (e: Exception) {
            e.printStackTrace()
        }

        val userDataProvider = UserDataProvider.getInstance(context)

        val mobile = userDataProvider.getAccessToken()
        if (mobile.isBlank()) {
            gotToWelcome()
            return
        }

        dataViewModel.initData(context)
        dataViewModel.isLoading.observe(this, {
            if (it) {
                return@observe
            }

            MainActivity.start(context)
            this@SplashActivity.finish()

        })


        val uiApiCallResponseListener = object : UIApiCallResponseListener {
            override fun onFailed(msg: String) {
                MyMessage.showToast(context, "Failed to get user info")
            }
            override fun onSuccess(msg: String) {
                dataViewModel.loadData()
            }
        }

        val tokenRefreshListener = object : UIApiCallResponseListener {
            override fun onFailed(msg: String) {
                MyMessage.showToast(context, msg)
            }

            override fun onSuccess(msg: String) {
                dataViewModel.loadUser(uiApiCallResponseListener)
            }

        }

        dataViewModel.refreshToken(tokenRefreshListener)
    }

    private fun gotToWelcome() {

        Handler(Looper.getMainLooper()).postDelayed({
            SignInActivity.start(context)
            this@SplashActivity.finish()
        }, 1500)

    }

}