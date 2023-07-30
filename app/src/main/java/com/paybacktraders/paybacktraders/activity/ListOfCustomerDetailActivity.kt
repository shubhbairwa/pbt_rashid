package com.paybacktraders.paybacktraders.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.ClientAdapter
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivityListOfCustomerDetailBinding
import com.paybacktraders.paybacktraders.fragments.ClientFragment
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
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

class ListOfCustomerDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityListOfCustomerDetailBinding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var id = 0
    var hashMap = HashMap<String, Any>()
    var announcementAdapter = ClientAdapter()
    private var localClientList: MutableList<DataCLient> = mutableListOf()


    var startDate = ""
    var endDate = ""

    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null
    var brokerList = arrayListOf<String>()
    var brokerName = ""

    var statusList =
        arrayListOf<String>("All", "Pending", "Approved", "Rejected", "Connect", "Disconnect")
    var statusName = "All"


    companion object {
        private const val TAG = "ListOfCustomerDetailAct"
    }

    private fun filterList(query: String) {
        val filteredList = localClientList.filter { item ->
            item.FullName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        announcementAdapter.filterData(filteredList)
    }

    private fun setUpLoadingDialog(context: Context) {
        builderAlert = AlertDialog.Builder(context)
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
        alertDialog = builderAlert!!.create()
    }

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
        binding = ActivityListOfCustomerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpLoadingDialog(this)
        supportActionBar?.show()
        setSupportActionBar(binding.toolbar)

        id = intent.getIntExtra(Global.ID, 0)

        binding.toolbar.setOnClickListener {
            finish()
        }




        announcementAdapter.setOnFullItemClickListener { data ->
            val bundle = Bundle().apply {
                putParcelable("data", data)
            }
            Intent(this, CustomerDetailsActivity::class.java).also {
                it.putExtras(bundle)
                startActivity(it)

            }
        }





        if (where.equals("admin", ignoreCase = true)) {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = id
            hashMap[Global.PAYLOAD_TYPE] = "All"
            hashMap[Global.PAYLOAD_FROM_DATE] = startDate
            hashMap[Global.PAYLOAD_TO_DATE] = endDate

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
//            viewModel.getClientAll()
//            subscribeToObserver()
        } else {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = id
            hashMap[Global.PAYLOAD_TYPE] = statusName
            hashMap[Global.PAYLOAD_FROM_DATE] = startDate
            hashMap[Global.PAYLOAD_TO_DATE] = endDate

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
        }

        binding!!.linearFilterButton.setOnClickListener {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = id
            hashMap[Global.PAYLOAD_TYPE] = statusName
            hashMap[Global.PAYLOAD_FROM_DATE] = startDate
            hashMap[Global.PAYLOAD_TO_DATE] = endDate

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
            setUpRecyclerView()

        }

        announcementAdapter.setOnAttachmentClickListener {
            if (it.PaymentProof.isEmpty()) {
                Toasty.info(this, "NO Data Found").show()
            } else {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(Global.PDF_BASE_URL + it.PaymentProof))

                // Check if there's any app that can handle the Intent (e.g., Chrome)

                // Open the link in the Chrome browser
                startActivity(intent)
            }

        }

        announcementAdapter.setOnRemarkClickListener { data ->
            val bundle = Bundle().apply {
                putString("id", data.id.toString())
            }
            Intent(this, RemarksListOfClientActivity::class.java).also {
                it.putExtra(Global.ID, data.id)
                startActivity(it)
            }
            //  findNavController().navigate(R.id.dialogRemarkStatusBinding,bundle)
        }




        viewModel.getBrokerAll()
        subscribeToBrokerObserver()

        // viewModel.getDashboardData(hashMap)

        setUpRecyclerView()




        setupStatus()


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        //  val distItem=menu.findItem(R.id.distributorFragment).setVisible(false)

        val item: MenuItem = menu!!.findItem(R.id.action_settings)
        val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
        val itemDate: MenuItem = menu.findItem(R.id.dateMenu)
        val itemFilter: MenuItem = menu.findItem(R.id.filterMenu)
        val searchView = itemSearch?.actionView as SearchView


       // itemSearch.setIconTintList(ColorStateList.valueOf(resources.getColor(R.color.white)))

        val customIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_search)
        customIconDrawable?.let {
            // Set the desired color for the icon
            val iconColor = ContextCompat.getColor(this, R.color.white)
            DrawableCompat.setTint(it, iconColor)
        }

// Set the custom icon to the SearchView
        searchView.setIconifiedByDefault(false) // Expand the SearchView to show the custom icon
        searchView.setIconified(false) // Show the SearchView in its expanded state
        searchView.queryHint = "Search" // Set the hint text if needed
        searchView.setIconifiedByDefault(true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Filter the adapter based on the search query
                // masterDistributorAdapter.filter.filter(newText)
                //   Log.e(MasterDistributorFragment.TAG, "onQueryTextChange: $newText", )
                filterList(newText)
                return true
            }
        })
        item.isVisible = false
        itemDate.isVisible = true
        itemSearch.isVisible = false
        itemFilter.isVisible = true

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dateMenu -> {
                val builder = MaterialDatePicker.Builder.dateRangePicker()
                val now = Calendar.getInstance()
                builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

                val picker = builder.build()
                picker.show(supportFragmentManager!!, picker.toString())

                picker.addOnNegativeButtonClickListener { picker.dismiss() }
                picker.addOnPositiveButtonClickListener {
                    startDate = Global.formatDateFromMilliseconds(it.first)
                    endDate = Global.formatDateFromMilliseconds(it.second)
                    hashMap[Global.PAYLOAD_EMPLOYEE_ID] = id
                    hashMap[Global.PAYLOAD_TYPE] = statusName
                    hashMap[Global.PAYLOAD_FROM_DATE] = startDate
                    hashMap[Global.PAYLOAD_TO_DATE] = endDate

                    viewModel.getClientALlFilter(hashMap)
                    subscribeToFilterObserver()

                    //   Timber.i("The selected date range is ${it.first} - ${it.second}")

                }
                return true
            }

            R.id.filterMenu -> {
                Log.e(TAG, "onOptionsItemSelected: ")
                binding!!.apply {

                    if (linearFilters.visibility == View.GONE) {
                        linearFilters.visibility = View.VISIBLE
                    } else {
                        linearFilters.visibility = View.GONE
                    }

                }





                return true
            }

            R.id.searchMenu -> {
                //   Toasty.info(requireContext(),"Date").show()
                Intent(this, SearchActivity::class.java).also {
                    it.putExtra(Global.INTENT_WHERE, Global.SEARCH_CLIENT)
                    startActivity(it)

                }
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupStatus() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                statusList
            )
        binding!!.acStatus.setAdapter<ArrayAdapter<String>>(adapter)

        binding!!.acStatus.setOnItemClickListener { parent, view, position, id ->
            statusName = statusList[position]
        }
    }


    private fun subscribeToBrokerObserver() {
        viewModel.brokerAll.observe(this, Event.EventObserver(
            onError = {
                Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {

            }, {
                brokerList.clear()
                if (it.status.equals(200)) {
                    for (productName in it.data) {
                        brokerList.add(productName.BrokerName)
                    }
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_dropdown_item_1line,
                            brokerList
                        )
                    binding!!.acBrokerName.setAdapter<ArrayAdapter<String>>(adapter)

                    binding!!.acBrokerName.setOnItemClickListener { parent, view, position, id ->
                        brokerName = brokerList[position]
                    }
                } else {
                    Toasty.error(this, it.message, Toasty.LENGTH_SHORT).show()
                }
            }
        ))
    }

    private fun SubscribeToStatus() {
        viewModel.updateCustomerStatus.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it).show()
                Log.e(TAG, "SubscribeToStatus:$it ")
            }, onLoading = {
                Global.showDialog(this)

            }, {
                Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    Toasty.success(this, it.message, Toasty.LENGTH_SHORT).show()

                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }

    private fun setUpRecyclerView() = binding?.rvClientFragment?.apply {
        adapter = announcementAdapter
        layoutManager = LinearLayoutManager(this@ListOfCustomerDetailActivity)
    }


    private fun subscribeToFilterObserver() {


        viewModel.clientFilter.observe(this, Event.EventObserver(
            onError = {
                alertDialog!!.dismiss()
                Toasty.error(this, it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.dismiss()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        if (brokerName.isEmpty()) {
                            localClientList.clear()
                            localClientList.addAll(it.data)
                            announcementAdapter.announcement = it.data
                        } else {

                            var masterList = mutableListOf<DataCLient>()

                            for (master in it.data) {
                                if (master.BrokerName == brokerName) {

                                    masterList.add(master)
                                }
                            }
                            if (masterList.isEmpty()) {
                                binding!!.nodataFound.visibility = View.VISIBLE
                            } else {
                                binding!!.nodataFound.visibility = View.GONE
                            }
                            localClientList.clear()
                            localClientList.addAll(masterList)
                            announcementAdapter.announcement = masterList
                        }

                    }


                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }


    override fun onResume() {
        super.onResume()
//        brokerList.clear()
//        brokerList.add(0,"All")
//        brokerList.add(1,"Master Distributor")
//        brokerList.add(2,"Distributor")
        statusList.clear()
        statusList =
            arrayListOf<String>("All", "Pending", "Approved", "Rejected", "Connect", "Disconnect")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statusList
            )
        binding!!.acStatus.setSelection(0)
        binding!!.acStatus.setAdapter<ArrayAdapter<String>>(adapter)
        binding!!.acStatus.text = null
        binding!!.acBrokerName.text = null

        // binding!!.acStatus.setText("All")
    }

}