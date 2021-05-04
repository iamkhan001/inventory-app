package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.FragmentOrderListBinding
import com.nymbleup.inventory.models.orders.Orders
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.ui.adapters.OrderListAdapter
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel

class OrdersFragment : Fragment() {

    private var safebinding: FragmentOrderListBinding? = null
    private val binding get() = safebinding!!

    private val supportDataViewModel: SupportDataViewModel by activityViewModels()

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {
        override fun onFailed(msg: String) {

        }

        override fun onSuccess(msg: String) {

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        safebinding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val onItemClickListener = object : OnItemClickListener<Orders> {
            override fun onClick(item: Orders) {
                val action = OrdersFragmentDirections.actionNavOrdersToOrderDetailFragment(item)
                findNavController().navigate(action)
            }


        }
        val orderListAdapter = OrderListAdapter(requireContext(), onItemClickListener)
        binding.rvOrderList.adapter = orderListAdapter

        supportDataViewModel.mOrders.observe(viewLifecycleOwner, {
            Log.e("orderlist", "adapter >> ${it.size}")
            orderListAdapter.setDeta(it)
        })

        supportDataViewModel.loadOrders(uiApiCallResponseListener)
    }
}