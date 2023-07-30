package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.ui.AddProductActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.DistributorAdapter
import com.paybacktraders.paybacktraders.adapters.MasterDistributorAdapter
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityListOfDistributorBinding
import com.paybacktraders.paybacktraders.global.Global
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
import kotlin.collections.HashMap

class ListOfDistributorActivity : AppCompatActivity() {
    lateinit var binding:ActivityListOfDistributorBinding
    lateinit var viewModel: MainViewModel
    var hashMap = HashMap<String, Any>()
    var masterDistributorAdapter=DistributorAdapter()
    private var itemList: MutableList<DataEmployeeAll> = mutableListOf()
    var id=0

    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory = MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }


    private fun filterList(query: String) {
        val filteredList = itemList.filter { item ->
            item.FullName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        masterDistributorAdapter.filterData(filteredList)
    }

//    override fun onCreateOptionsMenu(menu: Menu) {
//        val item: MenuItem = menu.findItem(R.id.action_settings)
//        val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
//        item.isVisible = false
//        itemSearch.isVisible = true
//
//    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_drawer, menu)

               val item: MenuItem = menu!!.findItem(R.id.action_settings)
        val itemSearch: MenuItem = menu!!.findItem(R.id.searchMenu)
        val searchView = itemSearch?.actionView as SearchView
        item.isVisible = false
        itemSearch.isVisible = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Filter the adapter based on the search query
                // masterDistributorAdapter.filter.filter(newText)
                Log.e(TAG, "onQueryTextChange: $newText")
                filterList(newText)
                return true
            }
        })
        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.searchMenu -> {
                Intent(this, SearchActivity::class.java).also {
                    it.putExtra(Global.INTENT_WHERE, Global.SEARCH_DIST)
                    startActivity(it)
                }
                return true
            }
        }


        return super.onOptionsItemSelected(item)


    }

    companion object{
        private const val TAG = "ListOfDistributorActivi"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListOfDistributorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        supportActionBar?.show()
        setSupportActionBar(binding.toolbar)
        id=intent.getIntExtra(Global.ID,0)

        hashMap[Global.PAYLOAD_ID] = id
        // viewModel.getDashboardData(hashMap)
        viewModel.getDistributor(hashMap)
        subscribeToObserver()
        setUpRecyclerView()

        masterDistributorAdapter.setOnItemClickListener { data ->
            val bundle = Bundle().apply {
                putSerializable("dist", data)
            }

            Intent(this, DistributorDetailsActivity::class.java).also {
                //   it.putExtra(Global.INTENT_WHERE, Global.MASTER_DIST_STRING)
                it.putExtra("dist", data)
                startActivity(it)
            }
        }

    }


    private fun setUpRecyclerView() = binding?.rvDistributor?.apply {
        adapter = masterDistributorAdapter
        layoutManager = LinearLayoutManager(this@ListOfDistributorActivity)
    }


    private fun subscribeToObserver() {


        viewModel.employeeAll.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it).show()
            }, onLoading = {
                Global.showDialog(this)

            }, {
                Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        masterDistributorAdapter.masterDistributor = it.data
                    } else {
                        itemList.clear()
                        itemList.addAll(it.data)
                        binding!!.nodataFound.visibility = View.GONE
                        masterDistributorAdapter.masterDistributor = it.data
                    }


                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }
}