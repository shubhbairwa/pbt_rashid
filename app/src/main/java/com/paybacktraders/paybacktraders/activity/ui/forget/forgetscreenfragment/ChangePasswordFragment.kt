package com.paybacktraders.paybacktraders.activity.ui.forget.forgetscreenfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.activity.LoginActivity
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.activity.ui.forgot.ForgotPasswordActivity
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentChangePasswordBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty

class ChangePasswordFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var binding : FragmentChangePasswordBinding
    var flagData : String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        flagData = arguments?.getString("key_data")

        Log.e("Flag===>", flagData.toString())

        if (flagData.equals("DataFromOTPScreen")){
            viewModel = (activity as ForgotPasswordActivity).viewModel
            binding.toolbarChangePassword.visibility = View.GONE
            binding.llcForgot.visibility = View.VISIBLE
            binding.edOldPassword.visibility = View.GONE
        }
        else if (flagData.equals("DataForProfileUpdateScreen")){
            (activity as AppCompatActivity).supportActionBar?.hide()
            viewModel = (activity as NavigationDrawerActivity).viewModel
            binding.toolbarChangePassword.visibility = View.VISIBLE
            binding.llcForgot.visibility = View.GONE
            binding.edOldPassword.visibility = View.VISIBLE
        }

        binding.toolbarChangePassword.setOnClickListener {
            if (flagData.equals("DataForProfileUpdateScreen")){
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }

        binding.ivBackPress.setOnClickListener{
            if (flagData.equals("DataFromOTPScreen")){
                (activity as ForgotPasswordActivity).finish()
            }
        }


        binding.submitButton.setOnClickListener {
            if (flagData.equals("DataForProfileUpdateScreen")){
                if (Global.isPassMatch(requireActivity(), binding.edOldPassword,Prefs.getString(Global.Password)) &&
                    Global.isPassMatch(requireActivity(), binding.edNewPassword, binding.edConfirmPassword.text.toString())){
                    var jsonObject : JsonObject = JsonObject()
                    jsonObject.addProperty("Email", Prefs.getString(Global.Email))
                    jsonObject.addProperty("Password", binding.edConfirmPassword.text.toString())
                    viewModel.getPasswordChange(jsonObject)

                    bindToObserver()
                }else{
                    Toast.makeText(requireActivity(), "Old Password / New Password Not Matched", Toast.LENGTH_SHORT).show()
                }
            }
            else if (flagData.equals("DataFromOTPScreen")){
                if (Global.isPassMatch(requireActivity(), binding.edNewPassword, binding.edConfirmPassword.text.toString())){
                    var jsonObject : JsonObject = JsonObject()
                    jsonObject.addProperty("Email", Prefs.getString(Global.Email))
                    jsonObject.addProperty("Password", binding.edConfirmPassword.text.toString())
                    viewModel.getPasswordChange(jsonObject)

                    bindToObserver()
                }else{
                    Toast.makeText(requireActivity(), "Password Not Matched", Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

    //todo bind observer.
    private fun bindToObserver() {
        viewModel.passwordChange.observe(viewLifecycleOwner, Event.EventObserver(
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
                    if (flagData.equals("DataForProfileUpdateScreen")){
                        Toast.makeText(requireActivity(), "Password Change Successfully", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStackImmediate()
                    }
                    else if (flagData.equals("DataFromOTPScreen")){
                        Toast.makeText(requireActivity(), "Password Change Successfully", Toast.LENGTH_SHORT).show()
                        var intent = Intent(requireActivity(), LoginActivity::class.java)
                        (activity as ForgotPasswordActivity).startActivity(intent)
                        (activity as ForgotPasswordActivity).finish()
                    }

                }else {
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }


}