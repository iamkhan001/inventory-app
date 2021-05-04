package com.nymbleup.inventory.repo

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nymbleup.inventory.config.*
import com.nymbleup.inventory.models.Item
import com.nymbleup.inventory.models.Outlet
import com.nymbleup.inventory.models.User
import com.nymbleup.inventory.models.orders.Orders
import com.nymbleup.inventory.utils.DataConverter
import com.nymbleup.inventory.utils.MyDateTimeUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils.getDiffInDays
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.Executors

private const val TAG = "S-API"
class ScheduleApiRepository(context: Context) {

    val userDataProvider = UserDataProvider.getInstance(context.applicationContext)
    val storeDataProvider = StoreDataProvider.getInstance(context.applicationContext)
    val appDataProvider = AppDataProvider.getInstance(context.applicationContext)
    val dbHelper = DbHelper(context)

    private val apiInterface = ApiClientSecure(userDataProvider.getAccessToken()).getRetrofitInstance().create(ApiInterface::class.java)

    fun getOutlets(mList: MutableLiveData<ArrayList<Outlet>>?, responseListener: UIApiCallResponseListener) {

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONArray(response)

                    val list = DataConverter.toOutletList(data)
                    dbHelper.saveOutlets(list)
                    if (list.size == 1) {
                        TOTAL_STORES = list.size
                        storeDataProvider.saveStore(list[0])
                    }

                    Log.e(TAG,"OUTLETS > $TOTAL_STORES")

                    mList?.postValue(list)
                    responseListener.onSuccess("")

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

        val call = apiInterface.outlets()
        call.enqueue(apiResponseListener)

    }

    fun getStoreTiming(responseListener: UIApiCallResponseListener) {

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val obj = JSONObject(response)
                    val data = obj.getJSONArray("results")
                    val list = DataConverter.toStoreTimingList(data)
                    dbHelper.saveStoreTimings(list)
                    val applicableToAll = obj.getJSONObject("meta").getBoolean("applicabile_all")

                    storeDataProvider.saveApplicableToAll(applicableToAll)
                    responseListener.onSuccess("")
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

        if (storeDataProvider.getStoreId().isEmpty()) {
            Log.e(TAG,"storeDataProvider STORE IS NULL")
            responseListener.onSuccess("")
            return
        }

        val call = apiInterface.getStoreTiming(storeDataProvider.getStoreId())
        call.enqueue(apiResponseListener)
    }

    fun loadFunctions(responseListener: UIApiCallResponseListener) {

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONArray(response)

                    val list = DataConverter.toFunctionList(data)
                    dbHelper.saveFunction(list)
                    responseListener.onSuccess("")

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

        val parameters = HashMap<String, String>()
        parameters["outlet_id"] = storeDataProvider.getStoreId()

        val call = apiInterface.storeFunctions(parameters)
        call.enqueue(apiResponseListener)

    }

    fun loadDepartments(responseListener: UIApiCallResponseListener) {

        val apiResponseListener = object : ApiResponseListener {
            override fun onSuccess(response: String, code: Int) {
                try {
                    val data = JSONArray(response)

                    val list = DataConverter.toDepartmentList(data)
                    dbHelper.saveDepartments(list)
                    responseListener.onSuccess("")

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

        val parameters = HashMap<String, String>()
        parameters["outlet_id"] = storeDataProvider.getStoreId()

        val call = apiInterface.departments(parameters)
        call.enqueue(apiResponseListener)

    }

    fun clearData() {
        dbHelper.clearData()
    }

    fun getUserInfo(mUser: MutableLiveData<User?>?, uiApiCallResponseListener: UIApiCallResponseListener) {

        Log.e(TAG, "Load getUserInfo")

        val apiResponseListener = object : ApiResponseListener {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    response.body()?.let {
                        val user: User? = Gson().fromJson(it.string(), User::class.java)
                        mUser?.postValue(user)
                        user?.let {
                            userDataProvider.saveUserInfo(user)
                        }
                    }
                    uiApiCallResponseListener.onSuccess("")
                    return
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.e(TAG, "ERROR ON GET USER INFO")
                uiApiCallResponseListener.onFailed("Failed to get user info!")
            }

            override fun onFailed(msg: String) {
                uiApiCallResponseListener.onFailed("Failed to get user info, Please check your intenet access")
                Log.e(TAG, "FAILED TO GET USER INFO")
            }
        }

        val call = apiInterface.userInfo()
        call.enqueue(apiResponseListener)

    }

    fun getUserDetails(): User {
        return userDataProvider.getUser()
    }

    var compositeDisposable = CompositeDisposable()

    fun fetchCompanyInfo(responseListener: UIApiCallResponseListener) {

        compositeDisposable.add(apiInterface.companyInfoRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val response = it.string().trim()
                    Log.e(TAG, "fetchData > $response")
                    try {
                        storeDataProvider.saveCompanyInfo(DataConverter.toCompanyInfo(response))
                        responseListener.onSuccess("")
                        return@subscribe
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    responseListener.onFailed("Server Error")
                }, {

                    if (it is HttpException) {
                        val exception: HttpException = it
                        val response = exception.response()

                        Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                        responseListener.onFailed("Error")
                    }
                    it.printStackTrace()
                }))
    }

    fun linkDevice(responseListener: UIApiCallResponseListener) {

        val body = HashMap<String, Any>()

        body["registration_id"] = appDataProvider.getToken()!!
        body["device_id"] = appDataProvider.getDevId()
        body["type"] = "android"

        compositeDisposable.add(apiInterface.linkDevice(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val response = it.string().trim()
                Log.e(TAG, "fetchData > $response")
                try {

                    return@subscribe
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                responseListener.onFailed("Server Error")
            }, {

                if (it is HttpException) {
                    val exception: HttpException = it
                    val response = exception.response()

                    Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                    responseListener.onFailed("Error")
                }
                it.printStackTrace()
            }))
    }

    fun unLinkDevice(responseListener: UIApiCallResponseListener) {

        val body = HashMap<String, Any>()

        body["device_id"] = appDataProvider.getDevId()

        compositeDisposable.add(apiInterface.unLinkDevice(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val response = it.string().trim()
                Log.e(TAG, "fetchData > $response")
                try {
                    responseListener.onSuccess("")
                    return@subscribe
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                responseListener.onFailed("Server Error")
            }, {

                if (it is HttpException) {
                    val exception: HttpException = it
                    val response = exception.response()

                    Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                    responseListener.onFailed("Error")
                }
                it.printStackTrace()
            }))
    }

    fun loadInventory(mInventory: MutableLiveData<ArrayList<Item>>, responseListener: UIApiCallResponseListener) {

        if (storeDataProvider.getStoreId().isEmpty()) {
            return
        }

        val today = MyDateTimeUtils.getDateToday()

        compositeDisposable.add(apiInterface.getInventory(storeDataProvider.getStoreId(), today, today)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val response = it.string().trim()
                    Log.e(TAG, "fetchData > $response")
                    try {

                        val data = JSONArray(response)
                        val list = DataConverter.toInventory(data)
                        mInventory.postValue(list)
                        responseListener.onSuccess("")
                        return@subscribe
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    responseListener.onFailed("Server Error")
                }, {

                    if (it is HttpException) {
                        val exception: HttpException = it
                        val response = exception.response()

                        Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                        responseListener.onFailed("Error")
                    }
                    it.printStackTrace()
                }))

    }

    fun updateStockCount(stocks: ArrayList<HashMap<String, Any?>>, responseListener: UIApiCallResponseListener) {

        if (storeDataProvider.getStoreId().isEmpty()) {
            return
        }

        val today = MyDateTimeUtils.getDateToday()

        compositeDisposable.add(apiInterface.saveStockCount(storeDataProvider.getStoreId(), today, stocks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    try {
                        val response = it.string().trim()
                        Log.e(TAG, "fetchData > $response")
                        responseListener.onSuccess("")
                        return@subscribe
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    responseListener.onFailed("Server Error")
                }, {

                    if (it is HttpException) {
                        val exception: HttpException = it
                        val response = exception.response()

                        Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                        responseListener.onFailed("Error")
                    }
                    it.printStackTrace()
                }))

    }

    fun loadOrders(mOrders: MutableLiveData<ArrayList<Orders>>, responseListener: UIApiCallResponseListener){
        val outlets = arrayOf(storeDataProvider.getStoreId())

        val parameters = HashMap<String, Any?>()
        parameters["date_start"] = "2020-08-15"
        parameters["date_end"] =  "2020-11-24"
        parameters["outlet"] = outlets
        parameters["vendor"] = arrayListOf<String>()
        parameters["status"] = arrayListOf<String>()

        compositeDisposable.add(apiInterface.orderList(parameters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                try {
                    val response = it.string().trim()
                    Log.e(TAG, "fetchData > $response")
                    val array = JSONArray(response)
                    val list = DataConverter.toOrders(array)
                    mOrders.postValue(list)

                    return@subscribe
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                responseListener.onFailed("Server Error")
            }, {

                if (it is HttpException) {
                    val exception: HttpException = it
                    val response = exception.response()

                    Log.e(TAG,"Error: ${response?.code()} \n${response?.errorBody()}")
                    responseListener.onFailed("Error")
                }
                it.printStackTrace()
            }))

    }
}




