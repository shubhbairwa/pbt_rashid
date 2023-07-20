package com.paybacktraders.paybacktraders.activity


import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.databinding.ActivityNavigationDrawerBinding
import com.paybacktraders.paybacktraders.databinding.NavHeaderNavigationDrawerBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class NavigationDrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding

    lateinit var viewModel: MainViewModel
    var where = ""

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
        where = Prefs.getString(Global.INTENT_WHERE)
        setUpViewModel()
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)


        binding.appBarNavigationDrawer.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                com.paybacktraders.paybacktraders.R.id.profileFragment,
                R.id.EnquiryFragment,
                R.id.financeFragment,
                R.id.dashBoardFragment,
                R.id.masterDistributorFragment,
                R.id.distributorFragment,
                R.id.clientFragment,
                R.id.productFragment

            ), drawerLayout
        )

        if (where.equals("admin")) {
            hideDistributorItem()
        } else {
            hideMasterDistributorItem()
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // navView is NavigationView
        val viewHeader = binding.navView.getHeaderView(0)

// nav_header.xml is headerLayout
        val navViewHeaderBinding: NavHeaderNavigationDrawerBinding =
            NavHeaderNavigationDrawerBinding.bind(viewHeader)
        navViewHeaderBinding.tvUserName.text = Prefs.getString(Global.FullName)
        navViewHeaderBinding.tvUserEmail.text = Prefs.getString(Global.Email)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.navigation_drawer, menu)
//      //  val distItem=menu.findItem(R.id.distributorFragment).setVisible(false)
//        return true
//    }


    private fun hideDistributorItem() {
        val navigationView =
            findViewById<View>(com.paybacktraders.paybacktraders.R.id.nav_view) as NavigationView
        val nav_Menu: Menu = navigationView.getMenu()
        nav_Menu.findItem(com.paybacktraders.paybacktraders.R.id.distributorFragment).isVisible =
            false
    }

    private fun hideMasterDistributorItem() {
        val navigationView =
            findViewById<View>(com.paybacktraders.paybacktraders.R.id.nav_view) as NavigationView
        val nav_Menu: Menu = navigationView.getMenu()
        nav_Menu.findItem(com.paybacktraders.paybacktraders.R.id.masterDistributorFragment).isVisible =
            false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}