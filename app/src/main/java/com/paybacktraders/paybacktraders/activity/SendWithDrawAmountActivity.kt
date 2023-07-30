package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivitySendWithDrawAmountBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyAddDistributor
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SendWithDrawAmountActivity : AppCompatActivity() {
    lateinit var binding: ActivitySendWithDrawAmountBinding
    lateinit var viewModel: MainViewModel
    var amountINwallet=0

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory =
            MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendWithDrawAmountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()

        var hashMap=HashMap<String,Any>()
        hashMap[Global.PAYLOAD_ID] = Prefs.getInt(Global.ID)
        viewModel.getDashboardData(hashMap)
        subscribeToAmountDataObserver()

        binding.toolbarDistributor.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (confirmInput(
                    binding.etEnterAmount,
                    binding.etPassword,
                    binding.etRemarks

                )
            ) {
                if (binding.etEnterAmount.text.toString().toInt()>=50){

                    if (binding.etEnterAmount.text.toString().toInt()<=amountINwallet){
                        binding.apply {
                            var hashMap = HashMap<String, Any>()
                            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                            hashMap[Global.PAYLOAD_USDT] = binding.etPassword.text.toString()
                            hashMap[Global.PAYLOAD_AMOUNT] = binding.etEnterAmount.text.toString()
                            hashMap[Global.PAYLOAD_REMARKS] = binding.etRemarks.text.toString()
                            viewModel.sendWithDrawlrequest(hashMap)
                        }
                        subscribeToObserver()

                    }else{
                        Toasty.error(this,"InSufficient Amount").show()
                    }

                }else{
                    Toasty.error(this,"Minimum Amount is $50").show()
                }



            } else {

            }
        }
    }


    private fun subscribeToObserver() {
        viewModel.requestWithDrawl.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {
                Global.showDialog(this)
            }, {
                Global.hideDialog()
                if (it.status.equals(200)) {
                    Toasty.success(this, it.message, Toasty.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }


            }
        ))
    }

    private fun subscribeToAmountDataObserver() {
        viewModel.dashBOardData.observe(this, Event.EventObserver(
            onError = {
                //  Global.hideDialog()
                Toasty.error(this, it).show()
            }, onLoading = {
                // Global.showDialog(requireActivity())

            }, {
                //  Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    binding?.apply {
//                        tvClientCounter.text = it.data[0].totalCustomer.toString()
//                        tvDistributorCounter.text = it.data[0].totalDistributor.toString()
//                        tvProductCounter.text = it.data[0].totalProduct.toString()
//                        tvWalletCounter.text = "${'$'} ${it.data[0].totalAmountInWallet.toString()}"
                        amountINwallet=it.data[0].totalAmountInWallet.toInt()
                    }

                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))



    }

    private fun confirmInput(
        amount: TextInputEditText, usdtAddres: TextInputEditText, remark: TextInputEditText,

        ): Boolean {
        if (amount.text!!.isEmpty()) {
            amount.requestFocus()
            amount.error = "invalid"
            return false
        } else if (usdtAddres.text!!.isEmpty()) {
            usdtAddres.requestFocus()
            usdtAddres.error = "invalid"
            return false
        } else if (remark.text!!.isEmpty()) {
            remark.requestFocus()
            remark.error = "invalid"
            return false
        } else {

            return true
        }
    }
}