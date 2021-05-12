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
import androidx.navigation.fragment.findNavController
import com.mirobotic.dialog.myDialog.SweetAlertDialog
import com.nymbleup.inventory.databinding.FragmentOrderListBinding
import com.nymbleup.inventory.models.ScheduleView
import com.nymbleup.inventory.models.orders.Order
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.ui.adapters.OrderListAdapter
import com.nymbleup.inventory.ui.adapters.OutletsAdapter
import com.nymbleup.inventory.ui.adapters.calendar.DateRangeAdapter
import com.nymbleup.inventory.ui.dialogs.CalendarDialog
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.KeyboardUtils
import com.nymbleup.inventory.utils.MyDateTimeUtils

class OrdersFragment : Fragment() {

    private var safebinding: FragmentOrderListBinding? = null
    private val binding get() = safebinding!!


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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        safebinding = FragmentOrderListBinding.inflate(inflater, container, false)
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

        dataViewModel.isLoading.observe(viewLifecycleOwner, {

            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.filter.spnLocation.isEnabled = false
                binding.filter.imgFilter.isEnabled = false
                binding.filter.imgSearch.isEnabled = false
                binding.filter.imgSearch.visibility = View.VISIBLE
                binding.filter.viewSearch.visibility = View.GONE
                binding.filter.etSearch.setText("")
                binding.filter.etSearch.clearFocus()
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
                binding.filter.imgSearch.isEnabled = true
                binding.progressBar.visibility = View.GONE
                dataViewModel.loadOrders(uiApiCallResponseListener)
            }

        })


        val onItemClickListener = object : OnItemClickListener<Order> {
            override fun onClick(item: Order) {
                isReady = false
                val action = OrdersFragmentDirections.actionNavOrdersToOrderDetailFragment(item)
                findNavController().navigate(action)
            }
        }

        val orderListAdapter = OrderListAdapter(requireContext(), onItemClickListener)
        binding.rvOrderList.adapter = orderListAdapter

        dataViewModel.mOrders.observe(viewLifecycleOwner, {
            Log.e("orderlist", "adapter >> ${it.size}")
            orderListAdapter.setDeta(it)
        })

//        dataViewModel.loadOrders(uiApiCallResponseListener)
    }
}