package com.nymbleup.inventory.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nymbleup.inventory.repo.UserApiRepository
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private lateinit var userApiRepository: UserApiRepository

    fun initVm(context: Context) {
        userApiRepository = UserApiRepository.getInstance(context.applicationContext)
    }

    fun login(email: String, mobile: String, password: String, uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            userApiRepository.login(email, mobile, password, uiApiCallResponseListener)
        }
    }

    fun resetPassword(email: String, uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            userApiRepository.passwordReset(email, uiApiCallResponseListener)
        }
    }

}