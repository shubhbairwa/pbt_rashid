package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivityMainBinding
import com.paybacktraders.paybacktraders.global.Global
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_admin) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavAdmin, navController)


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
    }


}