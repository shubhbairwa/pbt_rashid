package com.paybacktraders.paybacktraders.activity.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityEditProfileBinding
import com.paybacktraders.paybacktraders.global.Global
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
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditProfileBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()

        binding.toolbarUpdateProfile.setOnClickListener {
            onBackPressed()
        }
        var jsonObject = JsonObject()
        jsonObject.addProperty("id", Prefs.getInt(Global.ID))
        viewModel.getProfileDetailOneApi(jsonObject)
        subscribeToObserver()

        binding.btnSave.setOnClickListener {
            if (confirmInput(binding.etEnterFullName, binding.etEnterEmail, binding.etEnterPhone, binding.etDeliveryAddress)) {
                binding.apply {
                    val bodyAddMasterDistributor = BodyUpdateDistributor(
                        CreatedBy = Prefs.getInt(Global.ID),
                        Role = Prefs.getString(Global.Role),
                        DeliveryAddress = etDeliveryAddress.text.toString(),
                        Email = etEnterEmail.text.toString(),
                        FullName = etEnterFullName.text.toString(),
                        Mobile = etEnterPhone.text.toString(),
                        Password = Prefs.getString(Global.Password),
                        ReportingTo = Prefs.getString(Global.ReportingTo).toInt(),
                        UserName = Prefs.getString(Global.UserName),
                        id = Prefs.getInt(Global.ID)
                    )
                    viewModel.updateDistributor(bodyAddMasterDistributor)
                }

                bindObserver()
            }

        }

    }

    private fun subscribeToObserver() {
        viewModel.profileDeatil.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this,it, Toasty.LENGTH_SHORT).show()
            },
            onLoading = {
                Global.showDialog(this)
            }, onSuccess = {
                    response ->
                Global.hideDialog()
                if (response.status == 200){
                    setData(response.data[0])
                }else {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory = MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }

    //todo bind observer...
    private fun bindObserver() {
        viewModel.updateDist.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {
                Global.showDialog(this)
            }, {
                Global.hideDialog()
                if (it.status.equals(200)) {
                    Prefs.putString(Global.FullName, binding.etEnterFullName.text.toString())
                    Prefs.putString(Global.Email, binding.etEnterEmail.text.toString())
                    Prefs.putString(Global.Mobile, binding.etEnterPhone.text.toString())
                    Prefs.putString(Global.DeliveryAddress, binding.etDeliveryAddress.text.toString())
                    Toasty.success(this, it.message, Toasty.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }

            }
        ))


    }

    private fun setData(data: DataEmployeeAll) {
        binding.etEnterFullName.setText(data.FullName)
        binding.etEnterEmail.setText(data.Email)
        binding.etEnterPhone.setText(data.Mobile)
        binding.etDeliveryAddress.setText(data.DeliveryAddress)

    }

    private fun confirmInput(fullName: TextInputEditText, enterEmail: TextInputEditText, enterPhone: TextInputEditText, enterDelivery: TextInputEditText): Boolean {
        if (fullName.text!!.isEmpty()) {
            fullName.requestFocus()
            fullName.error = "invalid"
            return false
        }  else if (enterEmail.text!!.isEmpty()) {
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