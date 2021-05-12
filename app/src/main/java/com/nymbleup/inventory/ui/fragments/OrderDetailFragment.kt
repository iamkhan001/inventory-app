package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.mirobotic.dialog.myDialog.SweetAlertDialog
import com.nymbleup.inventory.databinding.FragmentOrderDetailBinding
import com.nymbleup.inventory.models.ScheduleView
import com.nymbleup.inventory.models.orders.Order
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.ui.adapters.OrderDetailAdapter
import com.nymbleup.inventory.ui.adapters.OutletsAdapter
import com.nymbleup.inventory.ui.adapters.calendar.DateRangeAdapter
import com.nymbleup.inventory.ui.dialogs.CalendarDialog
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.KeyboardUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils

class OrderDetailFragment : Fragment() {

    private var safeBinding : FragmentOrderDetailBinding? = null
    private val binding get() = safeBinding!!

    private val handler = Handler(Looper.getMainLooper())
    private var dateRangeAdapter: DateRangeAdapter? = null

    private val dataViewModel: SupportDataViewModel by activityViewModels()
    private var alertDialog: SweetAlertDialog? = null
    private var isReady = false

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {
        override fun onFailed(msg: String) {
            isReady = true
            binding.filter.spnLocation.isEnabled = true
            binding.progressBar.visibility = View.GONE
            dataViewModel.mOrders.postValue(ArrayList())
        }

        override fun onSuccess(msg: String) {
            isReady = true
            binding.filter.spnLocation.isEnabled = true
            binding.progressBar.visibility = View.GONE
        }

    }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        safeBinding = FragmentOrderDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.filter.tvDate.visibility = View.GONE
        binding.filter.imgSearch.visibility = View.GONE



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

        binding.filter.spnLocation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (!isReady) {
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



        dataViewModel.isLoading.observe(viewLifecycleOwner, {

            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.filter.spnLocation.isEnabled = false
                binding.filter.imgFilter.isEnabled = false
                dataViewModel.mOrders.postValue(ArrayList())
                return@observe
            }
            dataViewModel.loadOrders(uiApiCallResponseListener)
        })

        dataViewModel.mSelectedStore.observe(viewLifecycleOwner, {

            val loading = dataViewModel.isLoading.value ?: false

            if (!loading) {
                binding.filter.spnLocation.isEnabled = true
                binding.filter.imgFilter.isEnabled = true
                binding.progressBar.visibility = View.GONE
                dataViewModel.loadOrders(uiApiCallResponseListener)
            }

        })



//
        val order = requireArguments().getSerializable("order") as Order

        binding.tvPoNumberDetail.text = order.code
        binding.tvCreateOnDetail.text = MyDateTimeUtils.formatDate(order.createdOn)
        binding.tvDeliveryDateDetail.text = MyDateTimeUtils.formatDate(order.dateDelivered)
        binding.tvOrderStatusDetail.text = order.status
        binding.tvItemCount.text = "${order.items.size} items"

        val orderDetailAdapter = OrderDetailAdapter(requireContext(),order.status)
        binding.rvOrderDetail.adapter = orderDetailAdapter
        orderDetailAdapter.setData(order.items)

    }
}