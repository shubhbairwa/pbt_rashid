package com.paybacktraders.paybacktraders.activity


import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import com.paybacktraders.paybacktraders.PayBackTradersApplication
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityLoginBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var selectedItem = ""
    lateinit var viewModel: MainViewModel


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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        binding.etUserName.setText("admin@gmail.com")
        binding.etPassword.setText("admin@123")
        //Global.showDialog(this)
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(2000)
//            Global.hideDialog()
//        }


        setupUserTypeDropDown()


        binding.loginButton.setOnClickListener {
            var hashMap = HashMap<String, Any>()
            hashMap.put("UserName", binding.etUserName.text.toString())
            hashMap.put("Password", binding.etPassword.text.toString())
            viewModel.doLogin(hashMap)

//
//

        }

        subscribeToObserver()
    }

    private fun subscribeToObserver() {
        viewModel.login.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this,it,Toasty.LENGTH_SHORT).show()
               // Toasty.makeText(this, it, Toast.LENGTH_SHORT).show()
            }, onLoading = {
                Global.showDialog(this)
            }, {
                Global.hideDialog()
                if (it.status.equals(200)) {
                    Prefs.putInt(Global.ID, it.data[0].id)
                    Prefs.putString(Global.EmployeeCode, it.data[0].EmployeeCode)
                    Prefs.putString(Global.UserName, it.data[0].UserName)
                    Prefs.putString(Global.Password, it.data[0].Password)
                    Prefs.putString(Global.FullName, it.data[0].FullName)
                    Prefs.putString(Global.Email, it.data[0].Email)
                    Prefs.putString(Global.Mobile, it.data[0].Mobile)
                    Prefs.putString(Global.Role, it.data[0].Role)
                    Prefs.putString(Global.CreatedBy, it.data[0].CreatedBy)
                    Prefs.putString(Global.Status, it.data[0].Status)
                    Prefs.putString(Global.ReportingTo, it.data[0].ReportingTo)
                    Prefs.putString(Global.DeliveryAddress, it.data[0].DeliveryAddress)
                    Prefs.putString(Global.WalletAmount, it.data[0].WalletAmount)
                    Prefs.putString(Global.Datetime, it.data[0].Datetime)
                    Toasty.success(this,it.message,Toasty.LENGTH_SHORT).show()
                    // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                    Intent(this, NavigationDrawerActivity::class.java).also {
//                        Prefs.putString(Global.INTENT_WHERE,"admin")
//                     //   it.putExtra(Global.INTENT_WHERE,"admin")
//                        startActivity(it)
//                        finish()
//                    }

                    if (it.data[0].Role.equals("Admin", ignoreCase = true)) {
                        Intent(this, NavigationDrawerActivity::class.java).also {
                            Prefs.putString(Global.INTENT_WHERE,"admin")
                            startActivity(it)
                            finish()
                        }
                    } else {
                        Intent(this, NavigationDrawerActivity::class.java).also {
                            Prefs.putString(Global.INTENT_WHERE,"dist")
                            startActivity(it)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    private fun setupUserTypeDropDown() {
        // Create an array of items for the dropdown
        val items = resources.getStringArray(R.array.user_type)

        // Create an ArrayAdapter using the item array and a default dropdown layout
        val adapter = ArrayAdapter(
            this,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            items
        )

        // Set the adapter to the dropdown
        binding.dropdown.setAdapter(adapter)

        // Set item selector listener
        binding.dropdown.setOnItemClickListener { parent, view, position, id ->
            selectedItem = parent.getItemAtPosition(position).toString()
            // Do something with the selected item
            // For example, display a toast message
            Toast.makeText(this, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }


}