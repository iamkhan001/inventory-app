package com.nymbleup.inventory.ui.viewModels

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nymbleup.inventory.config.AppDataProvider
import com.nymbleup.inventory.config.StoreDataProvider
import com.nymbleup.inventory.config.UserDataProvider
import com.nymbleup.inventory.models.Outlet
import com.nymbleup.inventory.repo.ScheduleApiRepository
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    val mLocation = MutableLiveData<Location?>()

    companion object {
        private const val TAG = "MVM"
    }

    lateinit var scheduleApiRepository: ScheduleApiRepository
    private val mOutlets = MutableLiveData<ArrayList<Outlet>>()

    private lateinit var userDataProvider: UserDataProvider
    private lateinit var storeDataProvider: StoreDataProvider

    fun init(context: Context) {
        scheduleApiRepository = ScheduleApiRepository(context)
        userDataProvider = scheduleApiRepository.userDataProvider
        storeDataProvider = scheduleApiRepository.storeDataProvider
        updateToken(context)
    }

    fun getStoreId(): String = storeDataProvider.getStoreId()

    private fun updateToken(context: Context) {
        viewModelScope.launch {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                AppDataProvider(context).setToken(token)
                scheduleApiRepository.linkDevice(object : UIApiCallResponseListener {
                    override fun onFailed(msg: String) {
                        Log.e(TAG, "Failed to update Device Token: $msg")
                    }

                    override fun onSuccess(msg: String) {
                        Log.e(TAG, "Device Token updated")
                    }
                })
            })
        }
    }

    fun unlinkDevice(uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                scheduleApiRepository.unLinkDevice(responseListener = uiApiCallResponseListener)
            }
        }
    }


}