package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mirobotic.dialog.myDialog.SweetAlertDialog
import com.nymbleup.inventory.databinding.FragmentStockBinding
import com.nymbleup.inventory.models.ScheduleView
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.InventoryAdapter
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.ui.adapters.OutletsAdapter
import com.nymbleup.inventory.ui.adapters.calendar.DateRangeAdapter
import com.nymbleup.inventory.ui.dialogs.CalendarDialog
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.KeyboardUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils
import com.nymbleup.inventory.utils.MyMessage

class StockFragment : Fragment() {

    private val dataViewModel: SupportDataViewModel by activityViewModels()
    private var alertDialog: SweetAlertDialog? = null
    private var inventoryAdapter: InventoryAdapter? = null

    private val handler = Handler(Looper.getMainLooper())
    private var dateRangeAdapter: DateRangeAdapter? = null


    private val onDateRangeSelectedListener = object : CalendarDialog.OnDateRangeSelectedListener {

        override fun onRangeSelected(dateStart: String, dateEnd: String) {

        }

        override fun onDateSelected(date: String) {
            if (checkIfDataLoading()) {
                return
            }

            dateRangeAdapter?.selected(date)
            handler.postDelayed({ CalendarDialog.hide() }, 200)
        }

    }

    private fun checkIfDataLoading(): Boolean {
        return (dataViewModel.mIsLoadingSchedule.value == true || dataViewModel.isLoading.value == true)
    }

    private val stockApiCallResponseListener = object : UIApiCallResponseListener {

        override fun onFailed(msg: String) {
            alertDialog?.changeAlertType(SweetAlertDialog.ERROR_TYPE)
            alertDialog?.titleText = "Failed!"
            alertDialog?.contentText = msg
            alertDialog?.confirmText = "OK"
            alertDialog?.setConfirmClickListener {
                it.dismissWithAnimation()
                binding.btnUpdate.isEnabled = true
                inventoryAdapter?.disableClicks = false
            }
        }

        override fun onSuccess(msg: String) {
            alertDialog?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
            alertDialog?.titleText = "Success!"
            alertDialog?.contentText = msg
            alertDialog?.confirmText = "Stock items updated."
            alertDialog?.confirmText = "OK"
            alertDialog?.setConfirmClickListener {
                it.dismissWithAnimation()
                binding.btnUpdate.isEnabled = true
                inventoryAdapter?.disableClicks = false
                binding.progressBar.visibility = View.VISIBLE
                dataViewModel.loadInventory(uiApiCallResponseListener)
            }
        }
    }

    val uiApiCallResponseListener = object : UIApiCallResponseListener {
        override fun onFailed(msg: String) {
            isReady = true
            binding.progressBar.visibility = View.INVISIBLE
            MyMessage.showBar(view, msg)
            dataViewModel.mInventory.postValue(ArrayList())
            binding.btnUpdate.visibility = View.GONE
        }

        override fun onSuccess(msg: String) {
            binding.progressBar.visibility = View.INVISIBLE
            isReady = true
        }
    }

    companion object {
        private const val TAG = "DashBoard"
    }

    private var isReady = false

    private var safebinding: FragmentStockBinding? = null
    private val binding get() = safebinding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        safebinding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.mShowingDayDates.observe(viewLifecycleOwner, {
            binding.filter.tvDate.text = it
        })

        val dayClickListener = object : OnItemClickListener<String> {
            override fun onClick(item: String) {
                if (checkIfDataLoading()) {
                    return
                }
                dateRangeAdapter?.selected(item)
            }
        }

        dateRangeAdapter = DateRangeAdapter()
        dateRangeAdapter?.onDayClickListener = dayClickListener
        dateRangeAdapter?.selected(dataViewModel.dateStart)

        dataViewModel.mDateRange.observe(viewLifecycleOwner, {
            dateRangeAdapter?.setDataList(it)
        })

        binding.filter.tvDate.setOnClickListener {

            CalendarDialog.show(
                ScheduleView.DAY,
                dataViewModel.dateStart,
                childFragmentManager,
                onDateRangeSelectedListener
            )
        }

        binding.filter.tvDate.text = MyDateTimeUtils.getDatePretty()




        dataViewModel.mOutlets.observe(viewLifecycleOwner, {
            val outletAdapter = OutletsAdapter(it)
            binding.filter.spnLocation.adapter = outletAdapter

            dataViewModel.mSelectedStore.value?.let { store ->
                var selected = 0
                val storeId = store.id

                for ((i, outlet) in it.withIndex()) {
                    if (outlet.id == storeId) {
                        selected = i
                        break
                    }
                }

                binding.filter.spnLocation.setSelection(selected)
            }


            Log.e("mOutlets", "total ${it.size}")
        })

        binding.filter.spnLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {

                if (!isReady){
                    return
                }
                dataViewModel.mOutlets.value?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    dataViewModel.setSelectedStore(it[position])
                }
                isReady = false

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.filter.tvDate.text = MyDateTimeUtils.getDatePretty()

        binding.filter.imgSearch.setOnClickListener {
            it.visibility = View.GONE
            binding.filter.viewSearch.visibility = View.VISIBLE
            binding.filter.etSearch.requestFocus()
            KeyboardUtils.show(requireContext())
        }

        binding.filter.imgClear.setOnClickListener {
            binding.filter.imgSearch.visibility = View.VISIBLE
            binding.filter.viewSearch.visibility = View.GONE
            binding.filter.etSearch.setText("")
            binding.filter.etSearch.clearFocus()
            KeyboardUtils.hide(requireContext())
        }

        binding.filter.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        inventoryAdapter = InventoryAdapter(requireContext())
        binding.rvList.adapter = inventoryAdapter

        dataViewModel.mInventory.observe(viewLifecycleOwner, {
            binding.tvTotal.text = it.size.toString()
            inventoryAdapter?.setData(it)

            Log.e(TAG,"update > ${it.size}")

            if (it.isEmpty()) {
                binding.btnUpdate.visibility = View.GONE
            }else {
                binding.btnUpdate.visibility = View.VISIBLE
            }

        })

        dataViewModel.isLoading.observe(viewLifecycleOwner, {

            binding.progressBar.visibility = View.VISIBLE

            if (it) {
                binding.filter.spnLocation.isEnabled = false
                binding.filter.imgFilter.isEnabled = false
                binding.filter.imgSearch.isEnabled = false
                binding.filter.imgSearch.visibility = View.VISIBLE
                binding.filter.viewSearch.visibility = View.GONE
                binding.filter.etSearch.setText("")
                binding.filter.etSearch.clearFocus()
                dataViewModel.mInventory.postValue(ArrayList())
               return@observe
            }
            binding.filter.spnLocation.isEnabled = true
            binding.filter.imgFilter.isEnabled = true
            binding.filter.imgSearch.isEnabled = true
            dataViewModel.loadInventory(uiApiCallResponseListener)
        })

        dataViewModel.mSelectedStore.observe(viewLifecycleOwner, {
            val loading = dataViewModel.isLoading.value ?: false

            if (!loading) {
                binding.filter.spnLocation.isEnabled = true
                binding.filter.imgFilter.isEnabled = true
                binding.filter.imgSearch.isEnabled = true
                binding.progressBar.visibility = View.GONE
                dataViewModel.loadInventory(uiApiCallResponseListener)
            }

        })

        binding.btnUpdate.setOnClickListener {
            inventoryAdapter?.let {
                val list = it.getData()
                val stocks = ArrayList<HashMap<String, Any?>>()

                for (item in list ) {
                    val stock = HashMap<String, Any?>()
                    stock["batch"] = item.id
                    stock["article"] = item.articleId
                    stock["old_quantity"] = item.quantity
                    stock["old_loose_quantity"] = item.looseQuantity
                    stock["quantity"] = item.newPackQty
                    stock["loose_quantity"] = item.newLooseQty
                    stock["reason"] = "other"

                    stocks.add(stock)
                }

                it.disableClicks = true
                binding.btnUpdate.isEnabled = false

                alertDialog = MyMessage.getProgressAlert(requireContext(), "Updating stocks")
                alertDialog?.show()

                dataViewModel.updateStockCount(stocks, stockApiCallResponseListener)
            }

        }
    }

}