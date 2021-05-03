package com.nymbleup.inventory.config

import android.content.Context
import android.util.Log
import com.nymbleup.inventory.models.User

class UserDataProvider(context: Context) {

    companion object {

        fun getInstance(context: Context): UserDataProvider {

            var userDataProvider: UserDataProvider? = null

            if (userDataProvider == null) {
                userDataProvider = UserDataProvider(context)
            }

            return userDataProvider
        }

    }

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun setTokens(accessToken: String, refreshToken: String) {

        preferences.edit().apply {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()
        }

    }

    fun getAccessToken(): String = preferences.getString("accessToken", "")!!

    fun getRefreshToken(): String = preferences.getString("refreshToken", "")!!

    fun saveAccessToken(accessToken: String) {

        preferences.edit().apply {
            putString("accessToken", accessToken)
            apply()
        }
    }

    fun logout() {

        preferences.edit().apply {
            clear()
            apply()
        }

    }

    fun saveUserInfo(user: User) {

        val id = user.employeeId

        val enabled = if (user.enabled == null) {
            false
        }else {
            user.enabled!!
        }

        EMPLOYEE_ID = id

        Log.d("save","employee info > ${user.employeeId}")

        preferences.edit().apply {
            putString("id", user.id)
            putInt("employeeId", id)
            putString("firstName", user.firstName)
            putString("lastName", user.lastName)
            putString("phoneNumber", user.phoneNumber)
            putString("email", user.email)
            putString("mobile", user.mobile)
            putString("status", user.status)
            putString("designation", user.designation)
            putString("accessLevel", user.accessLevel)
            putBoolean("enabled", enabled)
            apply()
        }
    }

    fun getUser(): User {

        val user = User(
                preferences.getString("firstName", ""),
                preferences.getString("lastName", ""),
                preferences.getString("mobile", ""),
                preferences.getString("email", ""),
        )

        user.id = preferences.getString("id", "")
        user.employeeId = preferences.getInt("employeeId", 0)
        user.phoneNumber = preferences.getString("phoneNumber", "")
        user.status = preferences.getString("status", "")
        user.designation = preferences.getString("designation", "")
        user.accessLevel = preferences.getString("accessLevel", "")
        user.enabled = preferences.getBoolean("enabled", false)

        return user
    }

    fun doTokenRefresh(): Boolean {
        val time = System.currentTimeMillis()
        val lastTime = preferences.getLong("refreshTime", 0)
        val expiry = 86400000
        preferences.edit().apply {
            putLong("refreshTime", time)
            apply()
        }
        return (time - lastTime) > expiry
    }

}