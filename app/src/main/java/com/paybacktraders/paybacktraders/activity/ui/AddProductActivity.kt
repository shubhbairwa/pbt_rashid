package com.paybacktraders.paybacktraders.activity.ui

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityAddProductBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyForAddProduct
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding
    lateinit var viewModel: MainViewModel
    var brokerList = arrayListOf<String>()
    var brokerName = ""


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
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        viewModel.getBrokerAll()
        binding.apply {
            toolbarAddProduct.setOnClickListener { finish() }
        }
        subscribeToBrokerObserver()

        binding.btnAddProduct.setOnClickListener {
            binding.apply {


                if (confirmInput(
                        productCode = etProductCode,
                        productName = etProductName,
                        productFee = etProductFee,
                        MaxDD = etMaxDdInput,
                        profitMa = etProfitMa,
                        selectBroker = brokerName
                    )
                ) {
                    viewModel.addProduct(
                        BodyForAddProduct(
                            brokerName,
                            etMaxDdInput.text.toString(),
                            etProductCode.text.toString(),
                            etProductName.text.toString(),
                            etProfitMa.text.toString()
                        )
                    )
                    subscribeToAddProduct()
                }
            }

        }


    }

    private fun subscribeToAddProduct() {
        viewModel.addProduct.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {
                Global.showDialog(this)
            }, {
                Global.hideDialog()
                if (it.status.equals(200)) {
                    Toasty.success(this, "Added Successfully", Toasty.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }
            }
        ))
    }


    private fun confirmInput(
        productName: TextInputEditText,
        productCode: TextInputEditText,
        productFee: TextInputEditText,
        MaxDD: TextInputEditText,
        profitMa: TextInputEditText,
        selectBroker: String
    ): Boolean {

        if (productName.text!!.isEmpty()) {
            productName.requestFocus()
            productName.error = "invalid"
            return false
        } else if (productCode.text!!.isEmpty()) {
            productCode.requestFocus()
            productCode.error = "invalid"
            return false
        } else if (productFee.text!!.isEmpty()) {
            productFee.requestFocus()
            productFee.error = "invalid"
            return false
        } else if (MaxDD.text!!.isEmpty()) {
            MaxDD.requestFocus()
            MaxDD.error = "invalid"
            return false
        } else if (profitMa.text!!.isEmpty()) {
            profitMa.requestFocus()
            profitMa.error = "invalid"
            return false
        } else if (selectBroker.isEmpty()) {
            binding.selectBrokerInputLayout.requestFocus()
            binding.selectBrokerInputLayout.error = "invalid"
            return false
        } else {

            return true
        }
    }

    private fun subscribeToBrokerObserver() {
        viewModel.brokerAll.observe(this, Event.EventObserver(
            onError = {
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {

            }, {
                if (it.status.equals(200)) {
                    for (productName in it.data) {
                        brokerList.add(productName.BrokerName)
                    }
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this,
                            R.layout.simple_dropdown_item_1line,
                            brokerList
                        )
                    binding.acBrokerName.setAdapter<ArrayAdapter<String>>(adapter)

                    binding.acBrokerName.setOnItemClickListener { parent, view, position, id ->
                        brokerName = brokerList[position]
                    }
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }
            }
        ))
    }


}