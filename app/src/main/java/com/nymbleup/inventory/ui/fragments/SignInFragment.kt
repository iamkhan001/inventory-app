package com.nymbleup.inventory.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.FragmentSignInBinding
import com.nymbleup.inventory.ui.viewModels.LoginViewModel
import com.nymbleup.inventory.utils.Helpers.validateEmailAddress
import com.nymbleup.inventory.utils.MyMessage
import com.nymbleup.inventory.repo.UIApiCallResponseListener
import com.nymbleup.inventory.ui.activities.SplashActivity

class SignInFragment : Fragment() {

    private var safebinding: FragmentSignInBinding? = null
    private val binding get() = safebinding!!
    private val loginViewModel: LoginViewModel by viewModels()

    private val uiApiCallResponseListener = object : UIApiCallResponseListener {

        override fun onFailed(msg: String) {
            MyMessage.showBar(requireView(), msg)
            binding.progressBar.visibility = View.INVISIBLE
            binding.btnSignIn.isEnabled = true
            binding.tvForgetPassword.isEnabled = true
            MyMessage.showFailed(requireContext(), msg)
        }

        override fun onSuccess(msg: String) {
//            MyMessage.showToast(requireContext(), msg)
            SplashActivity.start(requireContext())
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        safebinding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.initVm(requireContext())

        binding.imgBack.setOnClickListener { activity?.onBackPressed() }

        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        }

        binding.btnSignIn.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.textEmail.error = "Enter email"
                return@setOnClickListener
            }

            val mobile = ""
            /*
            if (isOnlyNumbers(text)) {
                mobile = text

                if (mobile.length != 10) {
                    binding.textEmail.error = "Invalid mobile"
                    return@setOnClickListener
                }

            }else {

                email = text

                if (!validateEmailAddress(email)) {
                    binding.textEmail.error = "Invalid email"
                    return@setOnClickListener
                }

            }

             */

            if (!validateEmailAddress(email)) {
                binding.textEmail.error = "Invalid email"
                return@setOnClickListener
            }

            binding.textEmail.error = null

            val password = binding.etPassword.text.toString().trim()

            if (password.isEmpty()) {
                binding.textPassword.error = "Enter password"
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.btnSignIn.isEnabled = false
            binding.tvForgetPassword.isEnabled = false

            loginViewModel.login(email, mobile, password, uiApiCallResponseListener)

        }


    }

}