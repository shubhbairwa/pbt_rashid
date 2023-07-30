package com.paybacktraders.paybacktraders.activity.ui.forget.forgetscreenfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.activity.ui.forgot.ForgotPasswordActivity
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentOTPVerifyBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty


class OTPVerifyFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var binding : FragmentOTPVerifyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPVerifyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ForgotPasswordActivity).viewModel

        val emailData = arguments?.getString("key_data")

        binding.ivBackPress.setOnClickListener{
            (activity as ForgotPasswordActivity).finish()
        }

        binding.submitButton.setOnClickListener {
            var jsonObject : JsonObject = JsonObject()
            jsonObject.addProperty("Email", emailData)
            jsonObject.addProperty("OTP", binding.otpView.otp.toString())
            viewModel.getOtpVerify(jsonObject)

            bindToObserver()

        }

        binding.tvResendOtp.setOnClickListener {
            binding.otpView.setOTP("")
            var jsonObject : JsonObject = JsonObject()
            jsonObject.addProperty("Email", emailData)
            viewModel.getForgotPasswordEmail(jsonObject)
            resendOtpOberver()
        }




    }


    //todo bind observer.
    private fun bindToObserver() {
        viewModel.otpVerify.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireActivity(),it, Toasty.LENGTH_SHORT).show()
            },
            onLoading = {
                Global.showDialog(requireActivity())
            }, onSuccess = {
                    response ->
                Global.hideDialog()
                if (response.status == 200){
                    Toast.makeText(requireActivity(), "Otp Successfully verified.", Toast.LENGTH_SHORT).show()
                    (activity as ForgotPasswordActivity).switchFragmentWithData(
                        ChangePasswordFragment(), "DataFromOTPScreen")
                }else {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    //todo resend otp..
    private fun resendOtpOberver() {
        viewModel.forgotPasswordEmail.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireActivity(),it, Toasty.LENGTH_SHORT).show()
            },
            onLoading = {
                Global.showDialog(requireActivity())
            }, onSuccess = {
                    response ->
                Global.hideDialog()
                if (response.status == 200){
                    Toast.makeText(requireActivity(), "Otp Successfully Sent to your Email.", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    override fun onResume() {
        super.onResume()
        binding.otpView.setOTP("")
    }

}