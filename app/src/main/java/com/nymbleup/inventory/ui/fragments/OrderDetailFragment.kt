package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nymbleup.inventory.databinding.FragmentOrderDetailBinding
import com.nymbleup.inventory.models.orders.Order
import com.nymbleup.inventory.ui.adapters.OrderDetailAdapter
import com.nymbleup.inventory.utils.MyDateTimeUtils

class OrderDetailFragment : Fragment() {

    private var safeBinding : FragmentOrderDetailBinding? = null
    private val binding get() = safeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        safeBinding = FragmentOrderDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val order = requireArguments().getSerializable("order") as Order

        binding.tvPoNumberDetail.text = order.code
        binding.tvCreateOnDetail.text = MyDateTimeUtils.formatDate(order.createdOn)
        binding.tvDeliveryDateDetail.text = MyDateTimeUtils.formatDate(order.dateDelivered)
        binding.tvOrderStatusDetail.text = order.status
        binding.tvItemCount.text = "${order.items.size}"

        val orderDetailAdapter = OrderDetailAdapter(requireContext())
        binding.rvOrderDetail.adapter = orderDetailAdapter
        orderDetailAdapter.setData(order.items)

    }
}