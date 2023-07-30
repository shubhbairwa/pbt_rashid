package com.paybacktraders.paybacktraders.activity.ui.forgot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.databinding.ActivityForgotPasswordBinding
import com.paybacktraders.paybacktraders.fragments.forgotScreenfragment.ForgotPasswordFragment
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var biding : ActivityForgotPasswordBinding
    lateinit var viewModel: MainViewModel

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(biding.root)

        setUpViewModel()

        if (savedInstanceState == null) {
            // Load the initial fragment when the activity is created for the first time
            loadFragment(ForgotPasswordFragment())
        }
    }

    // Function to load a fragment into the container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.forgot_activity_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    // Function to switch between fragments with data parse..
    fun switchFragmentWithData(fragment: Fragment, dataToSend: String) {
        val args = Bundle()
        args.putString("key_data", dataToSend)
        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .replace(R.id.forgot_activity_container, fragment)
            .addToBackStack(null) // Optional: Add the fragment transaction to the back stack
            .commit()
    }

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory = MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }


}