package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.pedant.SweetAlert.SweetAlertDialog
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivityMasterDistributorBinding
import com.paybacktraders.paybacktraders.global.Global
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MasterDistributorActivity : AppCompatActivity() {
    lateinit var binding:ActivityMasterDistributorBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMasterDistributorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_distributor) as NavHostFragment
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


    }




}