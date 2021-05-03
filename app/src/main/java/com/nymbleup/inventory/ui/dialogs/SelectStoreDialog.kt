package com.nymbleup.inventory.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.FragmentSelectStoreBinding
import com.nymbleup.inventory.models.Outlet
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.adapters.OnItemClickListener
import com.nymbleup.inventory.ui.adapters.StoreAdapter
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.MyMessage
import com.nymbleup.inventory.views.ViewUtils

class SelectStoreDialog : BottomSheetDialogFragment() {

    companion object {

        private var fragment: SelectStoreDialog? = null

        fun show(fragmentManager: FragmentManager, isCancelable: Boolean) {
            hide()
            fragment = SelectStoreDialog()
            fragment?.isCancelable = isCancelable
            fragment?.show(fragmentManager, "openShift")
        }

        fun hide() {
            fragment?.dismiss()
        }

    }

    private var safebinding: FragmentSelectStoreBinding? = null
    private val binding get() = safebinding!!

    private val viewModel: SupportDataViewModel by activityViewModels()
    private val dataViewModel: SupportDataViewModel by activityViewModels()

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return BottomSheetDialog(requireContext(), R.style.MyTransparentBottomSheetDialogTheme)
//    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        safebinding = FragmentSelectStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        // Do something with your dialog like setContentView() or whatever
        return dialog
    }
    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }
    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val uiApiCallResponseListener = object : UIApiCallResponseListener {
            override fun onFailed(msg: String) {
                MyMessage.showToast(requireActivity(), msg)
            }

            override fun onSuccess(msg: String) {
            }
        }

        val itemClickListener = object : OnItemClickListener<Outlet> {
            override fun onClick(item: Outlet) {
                dismiss()
                viewModel.reset()
                dataViewModel.setSelectedStore(item)
            }
        }

        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadStores(uiApiCallResponseListener)

        ViewUtils.addDivider(binding.rvList)

        var storeAdapter: StoreAdapter? = null

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                storeAdapter?.filter?.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        viewModel.mStores.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE

            if (it.isEmpty()) {
                MyMessage.showBar(view, "Stores not found!")
                return@observe
            }

            if (it.size == 1) {
                itemClickListener.onClick(it[0])
                return@observe
            }

            binding.header.visibility = View.VISIBLE
            binding.etSearch.visibility = View.VISIBLE

            storeAdapter = StoreAdapter(it, itemClickListener)
            binding.rvList.adapter = storeAdapter
        })

    }

}