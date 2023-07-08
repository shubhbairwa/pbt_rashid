package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityAddDistributorBinding
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

class AddDistributorActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddDistributorBinding
    var where = ""
    lateinit var viewModel: MainViewModel
    var role = ""

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
        binding = ActivityAddDistributorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        where = intent.getStringExtra(Global.INTENT_WHERE).toString()
        role = if (where == Global.DISTRIBUTOR_STRING) {
            binding.toolbarDistributor.setTitle("Add Distributor")

            Global.DISTRIBUTOR_STRING
        } else {
            binding.toolbarDistributor.setTitle("Add Master Distributor")
            Global.MASTER_DIST_STRING
        }

        binding.toolbarDistributor.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (confirmInput(
                    binding.etEnterFullName,
                    binding.etPassword,
                    binding.etEnterEmail,
                    binding.etEnterPhone,
                    binding.etDeliveryAddress
                )
            ) {
                binding.apply {
                    val bodyAddDistributor = BodyAddDistributor(
                        CreatedBy = Prefs.getInt(Global.ID),
                        Role = role,
                        DeliveryAddress = etDeliveryAddress.text.toString(),
                        Email = etEnterEmail.text.toString(),
                        FullName = etEnterFullName.text.toString(),
                        Mobile = etEnterPhone.text.toString(),
                        Password = etPassword.text.toString(),
                        ReportingTo = 1,
                        UserName = etEnterFullName.text.toString()
                    )
                    val bodyAddMasterDistributor = BodyAddDistributor(
                        CreatedBy = Prefs.getInt(Global.ID),
                        Role = role,
                        DeliveryAddress = etDeliveryAddress.text.toString(),
                        Email = etEnterEmail.text.toString(),
                        FullName = etEnterFullName.text.toString(),
                        Mobile = etEnterPhone.text.toString(),
                        Password = etPassword.text.toString(),
                        ReportingTo = 1,
                        UserName = etEnterFullName.text.toString()
                    )
                    if (where == Global.DISTRIBUTOR_STRING) {
                        viewModel.addDistributor(bodyAddDistributor)

                    } else {
                        viewModel.addDistributor(bodyAddMasterDistributor)
                    }

                }
                subscribeToObserver()
            } else {

            }
        }

    }

    private fun subscribeToObserver() {
        viewModel.addDist.observe(this, Event.EventObserver(
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


    private fun confirmInput(
        fullName: TextInputEditText, enterPass: TextInputEditText, enterEmail: TextInputEditText,
        enterPhone: TextInputEditText, enterDelivery: TextInputEditText
    ): Boolean {
        if (fullName.text!!.isEmpty()) {
            fullName.requestFocus()
            fullName.error = "invalid"
            return false
        } else if (enterPass.text!!.isEmpty()) {
            enterPass.requestFocus()
            enterPass.error = "invalid"
            return false
        } else if (enterEmail.text!!.isEmpty()) {
            enterEmail.requestFocus()
            enterEmail.error = "invalid"
            return false
        } else if (enterPhone.text!!.isEmpty()) {
            enterPhone.requestFocus()
            enterPhone.error = "invalid"
            return false
        } else if (enterDelivery.text!!.isEmpty()) {
            enterDelivery.requestFocus()
            enterDelivery.error = "invalid"
            return false
        } else {

            return true
        }
    }

}