package com.nymbleup.inventory.ui.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nymbleup.inventory.config.StoreDataProvider
import com.nymbleup.inventory.models.*
import com.nymbleup.inventory.models.Function
import com.nymbleup.inventory.models.orders.Orders
import com.nymbleup.inventory.repo.ScheduleApiRepository
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.repo.UserApiRepository
import com.nymbleup.inventory.utils.MyDateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap

class SupportDataViewModel: ViewModel() {

    lateinit var scheduleApiRepository: ScheduleApiRepository
    private lateinit var userApiRepository: UserApiRepository

    val isLoading = MutableLiveData<Boolean>()

    private var apiCallCount = 0

    companion object {
        private const val TAG = "SVM"
        private const val TOTAL_API_CALLS = 5
    }

    val mOutlets = MutableLiveData<ArrayList<Outlet>>()
    var mSelectedStore = MutableLiveData<Outlet>()

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {
        override fun onFailed(msg: String) {
            checkStatus()
        }

        override fun onSuccess(msg: String) {
            checkStatus()
        }
    }

    fun initData(context: Context) {
        scheduleApiRepository = ScheduleApiRepository(context)
        userApiRepository = UserApiRepository(context)
    }

    fun init(context: Context, scheduleApiRepository: ScheduleApiRepository) {
        this.scheduleApiRepository = scheduleApiRepository
        this.userApiRepository = UserApiRepository(context)
        mSelectedStore.postValue(scheduleApiRepository.storeDataProvider.getStore())
        loadOutlets()
    }

    fun refreshToken(tokenRefreshListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            userApiRepository.refreshToken(tokenRefreshListener)
        }
    }

    fun setSelectedStore(item: Outlet) {
        Log.e(TAG, "Selected store ${item.id}")
        scheduleApiRepository.storeDataProvider.saveStore(item)
        mSelectedStore.postValue(item)
        loadData()
    }

    fun loadData() {

        val selectedStoreId = scheduleApiRepository.storeDataProvider.getStoreId()

        Log.e(TAG, "LOAD Data")
        if(selectedStoreId.isBlank()) {
            isLoading.value = false
            return
        }

        apiCallCount = 0

        isLoading.value = true
        scheduleApiRepository.clearData()

        val dateStart = MyDateTimeUtils.getLastMonthDate(-3)
        val dateEnd = MyDateTimeUtils.getNextMonthDate(3)

        Log.d(TAG,"start: $dateStart, end end: $dateEnd")
        viewModelScope.launch {
            scheduleApiRepository.getOutlets(mOutlets, uiApiCallResponseListener)
            scheduleApiRepository.loadDepartments(uiApiCallResponseListener)
            scheduleApiRepository.loadFunctions(uiApiCallResponseListener)
            scheduleApiRepository.fetchCompanyInfo(uiApiCallResponseListener)
            scheduleApiRepository.getStoreTiming(uiApiCallResponseListener)
        }

    }

    private fun checkStatus() {
        apiCallCount += 1
        Log.e(TAG,"checkStatus $apiCallCount / $TOTAL_API_CALLS")

        if (apiCallCount == TOTAL_API_CALLS) {
            isLoading.postValue(false)
        }
    }

    fun loadUser(uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            Log.e(TAG,"Load users")
            scheduleApiRepository.getUserInfo(null, uiApiCallResponseListener)
        }
    }

    private fun loadOutlets(){
        val list = scheduleApiRepository.dbHelper.getAllOutlets()
        mOutlets.postValue(list)
    }

    fun getFunctions(): ArrayList<Function> {
        val list = scheduleApiRepository.dbHelper.getAllFunctions()
        list.add(0, Function(0,"Select Function"))

        return list
    }

    fun getDepartments(): ArrayList<Department> {
        val list = scheduleApiRepository.dbHelper.getAllDepartments()
        list.add(0, Department("","Select Department"))

        return list
    }

    val mStores = MutableLiveData<ArrayList<Outlet>>()

    fun reset() {

    }

    fun loadStores(uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch(Dispatchers.IO){
            scheduleApiRepository.getOutlets(mStores, uiApiCallResponseListener)
        }
    }

    fun getStore(): StoreDataProvider {
        return scheduleApiRepository.storeDataProvider
    }

    fun getStoreId(): String {
        return scheduleApiRepository.storeDataProvider.getStoreId()
    }

    val mInventory = MutableLiveData<ArrayList<Item>>()

    fun loadInventory(uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            scheduleApiRepository.loadInventory(mInventory, uiApiCallResponseListener)
        }
    }

    fun updateStockCount(stocks: ArrayList<HashMap<String, Any?>>, uiApiCallResponseListener: UIApiCallResponseListener) {
        viewModelScope.launch {
            scheduleApiRepository.updateStockCount(stocks, uiApiCallResponseListener)
        }
    }
    val mOrders = MutableLiveData<ArrayList<Orders>>()


    fun loadOrders(uiApiCallResponseListener: UIApiCallResponseListener){
        viewModelScope.launch {
            scheduleApiRepository.loadOrders(mOrders,uiApiCallResponseListener)
        }
    }
}