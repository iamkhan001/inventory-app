package com.nymbleup.inventory.repo

import android.content.Context
import android.util.Log
import com.nymbleup.inventory.config.ApiClient
import com.nymbleup.inventory.config.ApiInterface
import com.nymbleup.inventory.config.UserDataProvider
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class UserApiRepository(context: Context) {

    private val apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface::class.java)
    private val userDataProvider = UserDataProvider.getInstance(context.applicationContext)

    fun login(email: String, mobile: String, password: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        if (email.isNotEmpty()) {
            parameters["email"] = email
        }else {
            parameters["mobile"] = mobile
        }
        parameters["password"] = password

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONObject(response)

                    val accessToken = data.getString("access")
                    val refreshToken = data.getString("refresh")

                    userDataProvider.setTokens(accessToken, refreshToken)
                    responseListener.onSuccess("Login success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Invalid phone/email or password")
            }

            override fun onFailed(msg: String) {
                if (msg.contains("internet")) {
                    responseListener.onFailed(msg)
                    return
                }
                responseListener.onFailed("Invalid phone/email or password")
            }
        }

        val call = apiInterface.login(parameters)
        call.enqueue(apiResponseListener)

    }

    fun passwordReset(email: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        parameters["email"] = email

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    responseListener.onSuccess("Success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.passwordReset(parameters)
        call.enqueue(apiResponseListener)

    }

    fun userStatus(email: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        parameters["email"] = email

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONObject(response)
                    val msg = data.getString("message")
                    responseListener.onSuccess(msg)
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.userStatus(parameters)
        call.enqueue(apiResponseListener)

    }

    fun resendActivationMail(email: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        parameters["email"] = email

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONObject(response)

                    responseListener.onSuccess("Success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.resendActivationMail(parameters)
        call.enqueue(apiResponseListener)

    }

    fun passwordResetConform(userId: String, token: String, newPassword: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        parameters["uid"] = userId
        parameters["token"] = token
        parameters["new_password"] = newPassword

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    responseListener.onSuccess("Success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.passwordResetConform(parameters)
        call.enqueue(apiResponseListener)

    }

    fun activateUser(userId: String, token: String, password: String, responseListener: UIApiCallResponseListener) {

        val parameters = HashMap<String, String>()
        parameters["uid"] = userId
        parameters["token"] = token
        parameters["password"] = password

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONObject(response)

                    responseListener.onSuccess("Success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.activateUser(parameters)
        call.enqueue(apiResponseListener)

    }


    fun refreshToken(responseListener: UIApiCallResponseListener) {

        if (!userDataProvider.doTokenRefresh()) {
            responseListener.onSuccess("")
            return
        }

        val parameters = HashMap<String, String>()
        parameters["refresh"] = userDataProvider.getRefreshToken()

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONObject(response)

                    val accessToken = data.getString("access")
                    userDataProvider.saveAccessToken(accessToken)

                    responseListener.onSuccess("Success")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }

            override fun onFailed(msg: String) {
                responseListener.onFailed(msg)
            }
        }

        val call = apiInterface.refreshToken(parameters)
        call.enqueue(apiResponseListener)

    }

    companion object {

        private const val TAG = "ApiRepository"

        private var userApiRepository: UserApiRepository? = null

        fun getInstance(context: Context): UserApiRepository {

            if (userApiRepository == null) {
                userApiRepository = UserApiRepository(context)
            }

            return userApiRepository!!
        }
    }


}