package com.nymbleup.inventory.config

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    //LOGIN / REGISTER
    @POST("api/v1/users/login/")
    fun login(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/status/")
    fun userStatus(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/activate/resend/")
    fun resendActivationMail(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/password/reset/")
    fun passwordReset(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/password/reset/confirm/")
    fun passwordResetConform(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/login/refresh/")
    fun refreshToken(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/users/activate/")
    fun activateUser(@Body body: HashMap<String, String>): Call<ResponseBody>

    @GET("api/v1/users/self/")
    fun userInfo(): Call<ResponseBody>

    @GET("api/v1/company/self/")
    fun companyInfoRx(): Observable<ResponseBody>

    @GET("/api/v1/outlet/{store}/inventory/stock-count/?expired=false&get_all=true")
    fun getInventory(@Path("store") store: String, @Query("from_date") dateFrom: String, @Query("to_date") dateTo: String,): Observable<ResponseBody>

    @POST("/api/v1/outlet/{store}/stock-count/{date}/save/")
    fun saveStockCount(@Path("store") store: String, @Path("date") date: String, @Body stocks: ArrayList<HashMap<String, Any?>>): Observable<ResponseBody>

    //GENERAL INFO
    @POST("api/v1/read/store-departments/")
    fun departments(@Body body: HashMap<String, String>): Call<ResponseBody>

    @GET("api/v1/outlet/")
    fun outlets(@Query("get_all") getAll: Boolean = true, @Query("type") type: String = "store"): Call<ResponseBody>

    @GET("api/v1/settings/business/days/get_all_store_times/")
    fun getStoreTiming(@Query("store") store: String): Call<ResponseBody>

    @POST("api/v1/read/store-functions/")
    fun storeFunctions(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/event/list/")
    fun events(@Query("get_all") getAll: Boolean = true, @Body body: HashMap<String, Any>): Call<ResponseBody>

    @POST("api/v1/read/store-staff-info/")
    fun storeStaffInfo(@Body body: HashMap<String, String>): Call<ResponseBody>

    @POST("api/v1/read/employees/")
    fun employees(@Body body: HashMap<String, String>): Call<ResponseBody>

    //Device
    @POST("api/v1/users/device/save/")
    fun linkDevice(@Body body: HashMap<String, Any>): Observable<ResponseBody>

    @POST("api/v1/users/device/unmap/")
    fun unLinkDevice(@Body body: HashMap<String, Any>): Observable<ResponseBody>


}