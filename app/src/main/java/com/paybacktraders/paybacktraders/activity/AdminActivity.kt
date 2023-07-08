package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.databinding.ActivityMainBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*


class AdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
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
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_admin) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavAdmin, navController)
        setUpViewModel()

        binding.ibLogoutAdmin.setOnClickListener {
            Global.showDialog(this)
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                Global.hideDialog()
                Prefs.clear()
                Intent(this@AdminActivity, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                Toasty.info(this@AdminActivity, "logOut Successfully")
            }


        }

        binding.tvToolbarDashboard.text = Prefs.getString(Global.FullName,"NOt Found")
    }


}