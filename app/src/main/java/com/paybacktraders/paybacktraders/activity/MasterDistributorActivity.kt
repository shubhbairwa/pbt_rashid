package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.pedant.SweetAlert.SweetAlertDialog
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.databinding.ActivityMasterDistributorBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*

class MasterDistributorActivity : AppCompatActivity() {
    lateinit var binding:ActivityMasterDistributorBinding
    lateinit var navController: NavController
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
        binding= ActivityMasterDistributorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_distributor) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavDistributor, navController)

        binding.ibLogoutAdmin.setOnClickListener {
            binding.ibLogoutAdmin.setOnClickListener {
                Global.showDialog(this)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    Global.hideDialog()
                    Prefs.clear()
                    Intent(this@MasterDistributorActivity, LoginActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    Toasty.info(this@MasterDistributorActivity, "logOut Successfully")
                }
            }
        }

        binding.tvToolbarDashboard.text = Prefs.getString(Global.FullName,"NOt Found")



    }




}