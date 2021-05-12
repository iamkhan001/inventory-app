package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mirobotic.dialog.myDialog.SweetAlertDialog
import com.nymbleup.inventory.databinding.FragmentOrderDetailBinding
import com.nymbleup.inventory.models.orders.Order
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.OrderDetailAdapter
import com.nymbleup.inventory.ui.adapters.OrderListAdapter.Companion.getOrderStatusName
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.MyDateTimeUtils
import com.nymbleup.inventory.utils.MyMessage

class OrderDetailFragment : Fragment() {

    private var safeBinding : FragmentOrderDetailBinding? = null
    private val binding get() = safeBinding!!

    private val dataViewModel: SupportDataViewModel by activityViewModels()
    private var alertDialog: SweetAlertDialog? = null
    private var isReady = false

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {
        override fun onFailed(msg: String) {
            isReady = true
            binding.progressBar.visibility = View.GONE
            dataViewModel.mOrders.postValue(ArrayList())
        }

        override fun onSuccess(msg: String) {
            isReady = true
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        safeBinding = FragmentOrderDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var orderDetailAdapter: OrderDetailAdapter? = null

    private val orderApiCallResponseListener = object : UIApiCallResponseListener {

        override fun onFailed(msg: String) {
            alertDialog?.changeAlertType(SweetAlertDialog.ERROR_TYPE)
            alertDialog?.titleText = "Failed!"
            alertDialog?.contentText = msg
            alertDialog?.confirmText = "OK"
            alertDialog?.setConfirmClickListener {
                it.dismissWithAnimation()
                binding.btnUpdate.isEnabled = true
                orderDetailAdapter?.disableClicks = false
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
                orderDetailAdapter?.disableClicks = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val order = requireArguments().getSerializable("order") as Order

        binding.tvPoNumberDetail.text = order.code
        binding.tvCreateOnDetail.text = MyDateTimeUtils.formatDate(order.createdOn)
        binding.tvDeliveryDateDetail.text = MyDateTimeUtils.formatDate(order.dateDelivered)
        binding.tvOrderStatusDetail.text = getOrderStatusName(order.status)
        binding.tvItemCount.text = "${order.items.size} items"

        val orderDetailAdapter = OrderDetailAdapter(order.status)
        binding.rvOrderDetail.adapter = orderDetailAdapter
        orderDetailAdapter.setData(order.items)

        if(order.items.size > 0) {
            binding.btnUpdate.visibility = View.VISIBLE

            binding.btnUpdate.setOnClickListener {

                alertDialog = MyMessage.getProgressAlert(requireContext(),"Updating Order Items")
                alertDialog?.show()

                val list = orderDetailAdapter.getList()

                val orderItems = ArrayList<HashMap<String, Any?>>()

                for (item in list ) {
                    val oItem = HashMap<String, Any?>()
                    oItem["id"] = item.id
                    oItem["order"] = order.id
                    oItem["quantity"] = item.newQty
                    oItem["unit"] = item.unit
                    oItem["unit_conversion"] = item.unitConversion
                    oItem["amount"] = item.amount
                    oItem["expiry_date"] = item.expiryDate
                    oItem["batch_number"] = item.batchNumber

                    orderItems.add(oItem)
                }

                orderDetailAdapter.disableClicks = true
                binding.btnUpdate.isEnabled = false

                dataViewModel.updateOrderItems(order.id, orderItems, orderApiCallResponseListener)

            }

        }

    }
}