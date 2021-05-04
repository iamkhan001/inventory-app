package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.FragmentOrderDetailBinding
import com.nymbleup.inventory.models.orders.Orders
import com.nymbleup.inventory.ui.adapters.OrderDetailAdapter

class OrderDetailFragment : Fragment() {

    private var safeBinding : FragmentOrderDetailBinding? = null
    private val binding get() = safeBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        safeBinding = FragmentOrderDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val order = requireArguments().getSerializable("orders") as Orders

        binding.tvPoNumberDetail.text = order.code
        binding.tvCreateOnDetail.text = order.createdOn
        binding.tvDeliveryDateDetail.text = order.dateDelivered
        binding.tvOrderStatusDetail.text = order.status
        binding.tvTotalItem.text = "Total : ${order.totalAmount}"





        val orderDetailAdapter = OrderDetailAdapter(requireContext())
        binding.rvOrderDetail.adapter = orderDetailAdapter
        orderDetailAdapter.setData(order.items)


    }
}