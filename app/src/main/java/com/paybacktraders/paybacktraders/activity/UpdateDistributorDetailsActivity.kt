package com.paybacktraders.paybacktraders.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityAddDistributorBinding
import com.paybacktraders.paybacktraders.databinding.ActivityUpdateDistributorDetailsBinding
import com.paybacktraders.paybacktraders.fragments.MasterDistributorFragment
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyAddDistributor
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyUpdateDistributor
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UpdateDistributorDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateDistributorDetailsBinding


    var where = ""
    var whereEditFromPopUp = ""
    lateinit var viewModel: MainViewModel
    var role = ""
    var dataEmployeeAll: DataEmployeeAll? = null

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory = MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDistributorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        dataEmployeeAll = intent.getSerializableExtra("dist") as DataEmployeeAll
        autoFillDataComingFromDistributorItem(dataEmployeeAll!!)

        where = intent.getStringExtra(Global.INTENT_WHERE).toString()
        whereEditFromPopUp = intent.getStringExtra(Global.INTENT_EDIT_WHERE).toString()
        role = if (where == Global.DISTRIBUTOR_STRING) {
            binding.toolbarDistributor.setTitle("Update Distributor")

            Global.DISTRIBUTOR_STRING
        } else {
            binding.toolbarDistributor.setTitle("Update Master Distributor")
            Global.MASTER_DIST_STRING
        }

        binding.toolbarDistributor.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (confirmInput(binding.etEnterFullName, binding.etPassword, binding.etEnterEmail, binding.etEnterPhone, binding.etDeliveryAddress)) {
                binding.apply {
                    val bodyAddDistributor = BodyUpdateDistributor(
                        CreatedBy = Prefs.getInt(Global.ID),
                        Role = role,
                        DeliveryAddress = etDeliveryAddress.text.toString(),
                        Email = etEnterEmail.text.toString(),
                        FullName = etEnterFullName.text.toString(),
                        Mobile = etEnterPhone.text.toString(),
                        Password = etPassword.text.toString(),
                        ReportingTo = 1,
                        UserName = etEnterFullName.text.toString(),
                        id = dataEmployeeAll!!.id
                    )
                    val bodyAddMasterDistributor = BodyUpdateDistributor(
                        CreatedBy = Prefs.getInt(Global.ID),
                        Role = role,
                        DeliveryAddress = etDeliveryAddress.text.toString(),
                        Email = etEnterEmail.text.toString(),
                        FullName = etEnterFullName.text.toString(),
                        Mobile = etEnterPhone.text.toString(),
                        Password = etPassword.text.toString(),
                        ReportingTo = 1,
                        UserName = etEnterFullName.text.toString(),
                        id = dataEmployeeAll!!.id
                    )
                    if (where == Global.DISTRIBUTOR_STRING) {
                        viewModel.updateDistributor(bodyAddDistributor)

                    } else {

                        viewModel.updateDistributor(bodyAddMasterDistributor)
                    }

                }
                subscribeToObserver()
            } else {

            }
        }
    }

    private fun autoFillDataComingFromDistributorItem(dataEmployeeAll: DataEmployeeAll) {
        binding.apply {
            etEnterFullName.setText(dataEmployeeAll.FullName)
            etPassword.setText(dataEmployeeAll.Password)
            etEnterEmail.setText(dataEmployeeAll.Email)
            etEnterPhone.setText(dataEmployeeAll.Mobile)
            etDeliveryAddress.setText(dataEmployeeAll.DeliveryAddress)

        }

    }

    private fun subscribeToObserver() {
        viewModel.updateDist.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {
                Global.showDialog(this)
            }, {
                Global.hideDialog()
                if (it.status.equals(200)) {
                    Toasty.success(this, it.message, Toasty.LENGTH_SHORT).show()
                    if (where == Global.DISTRIBUTOR_STRING){

                    }else{
                    //   MasterDistributorFragment().refreshFragment()
                    }
                    finish()
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }


            }
        ))
    }


    private fun confirmInput(fullName: TextInputEditText, enterPass: TextInputEditText, enterEmail: TextInputEditText, enterPhone: TextInputEditText, enterDelivery: TextInputEditText): Boolean {
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