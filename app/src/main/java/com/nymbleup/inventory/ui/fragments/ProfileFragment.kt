package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nymbleup.inventory.R
import com.nymbleup.inventory.ui.activities.SignInActivity
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel

class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private val dataViewModel: SupportDataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.scheduleApiRepository.storeDataProvider.logout()
        dataViewModel.scheduleApiRepository.appDataProvider.resetDevId()

        SignInActivity.start(requireContext())
        activity?.finish()

    }
}