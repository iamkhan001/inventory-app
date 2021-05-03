package com.nymbleup.inventory.repo

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ApiResponseListener"

interface ApiResponseListener: Callback<ResponseBody> {

    fun onSuccess(response: String, code: Int){}

    fun onFailed(msg: String){}

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

        try {
            val code = response.code()
            if (code == 200 || code == 201 || code == 204) {
                response.body()?.let {
                    val r = it.string()
                    Log.d(TAG,"Response OK: $r")
                    onSuccess(r, response.code())
                    return
                }
                onSuccess("", 200)
                return
            }

            val errorBody = response.errorBody()
            errorBody?.let {
                val jsonObject = JSONObject(it.string())
                Log.e(TAG,"Response Failed: ${it.string()}")
                onFailed(it.string())
                return
            }

            onSuccess("", response.code())
            return

        }catch (e: Exception) {
            e.printStackTrace()
        }
        onFailed("Error")
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        val msg = if(t.message == null) {
            "No internet access!"
        }else {
            t.message!!
        }
        Log.e(TAG, "onFailure: ${t.message}")
        onFailed(msg)
    }

}