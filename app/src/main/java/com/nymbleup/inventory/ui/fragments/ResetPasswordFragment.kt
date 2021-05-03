package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nymbleup.inventory.databinding.FragmentForgotPasswordBinding
import com.nymbleup.inventory.ui.viewModels.LoginViewModel
import com.nymbleup.inventory.utils.Helpers
import com.nymbleup.inventory.utils.MyMessage
import com.nymbleup.inventory.repo.UIApiCallResponseListener

class ResetPasswordFragment : Fragment() {

    private var safebinding: FragmentForgotPasswordBinding? = null
    private val binding get() = safebinding!!
    private val loginViewModel: LoginViewModel by viewModels()

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {

        override fun onFailed(msg: String) {
            MyMessage.showBar(requireView(), msg)
            binding.btnContinue.isEnabled = true
        }

        override fun onSuccess(msg: String) {
            MyMessage.showSuccess(requireContext(), "Email sent!", "We have sent email with password reset link. Please check your email")
            binding.btnContinue.isEnabled = true
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        safebinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener { activity?.onBackPressed() }

        loginViewModel.initVm(requireContext())

        binding.btnContinue.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.textEmail.error = "Enter email"
                return@setOnClickListener
            }

            if (!Helpers.validateEmailAddress(email)) {
                binding.textEmail.error = "Invalid email"
                return@setOnClickListener
            }

            binding.textEmail.error = null
            binding.btnContinue.isEnabled = false

            loginViewModel.resetPassword(email, uiApiCallResponseListener)

        }

    }

}