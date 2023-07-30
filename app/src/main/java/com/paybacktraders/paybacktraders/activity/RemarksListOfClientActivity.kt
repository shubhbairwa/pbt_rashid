package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.adapters.ClientStatusRemarkAdapter
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityRemarksListOfClientBinding
import com.paybacktraders.paybacktraders.dialogs.ClientStatusRemarkDialogArgs
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataClientStatusRemark
import com.paybacktraders.paybacktraders.repository.DefaultMainRepositories
import com.paybacktraders.paybacktraders.repository.MainRepos
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.paybacktraders.paybacktraders.viewmodel.MainViewModelProvider
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemarksListOfClientActivity : AppCompatActivity() {
    lateinit var binding:ActivityRemarksListOfClientBinding

    lateinit var viewModel: MainViewModel
 var id=0
    var clientStatusRemarkAdapter = ClientStatusRemarkAdapter()

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
        binding= ActivityRemarksListOfClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        id=intent.getIntExtra(Global.ID,0)
        var hashMap = HashMap<String, Any>()
        hashMap["CustomerId"] = id
        viewModel.getCustomerStatus(hashMap)
        subscribeToObserver()
        setUpRecyclerView()
        binding.toolbar.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView() = binding!!.recyclerViewRemarks.apply {
        adapter = clientStatusRemarkAdapter
        layoutManager = LinearLayoutManager(this@RemarksListOfClientActivity)
    }

    private fun subscribeToObserver() {
        viewModel.statusClientRemark.observe(this, Event.EventObserver(
            onError = {
                Toasty.error(this, it).show()
                binding.progress.visibility= View.GONE
            }, onLoading = {
                binding.progress.visibility= View.VISIBLE
            }, {
                binding.progress.visibility= View.GONE
                if (it.status.equals(200)) {

                    if (it.data.isEmpty()) {
                        binding.noDataFound.visibility = View.VISIBLE
                    } else {
                        binding.noDataFound.visibility = View.GONE

                        clientStatusRemarkAdapter.masterDistributor =
                            it.data as MutableList<DataClientStatusRemark>
                    }
                } else {
                    Toasty.error(this, it.message).show()
                }
            }
        ))
    }
}