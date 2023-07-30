package com.paybacktraders.paybacktraders.fragments.forgotScreenfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.activity.ui.forget.forgetscreenfragment.OTPVerifyFragment
import com.paybacktraders.paybacktraders.activity.ui.forgot.ForgotPasswordActivity
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentForgotPasswordBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty

class ForgotPasswordFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var binding : FragmentForgotPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ForgotPasswordActivity).viewModel

        binding.ivBackPress.setOnClickListener{
            (activity as ForgotPasswordActivity).finish()
        }

        binding.submitButton.setOnClickListener {
            var jsonObject : JsonObject = JsonObject()
            jsonObject.addProperty("Email", binding.edEmail.text.toString())
            viewModel.getForgotPasswordEmail(jsonObject)
            bindToObserver()
        }



    }

    //todo bind observer.
    private fun bindToObserver() {
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
                    (activity as ForgotPasswordActivity).switchFragmentWithData(OTPVerifyFragment(), binding.edEmail.text.toString())
                }else {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

}