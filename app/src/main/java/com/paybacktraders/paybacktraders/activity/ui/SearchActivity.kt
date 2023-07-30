package com.paybacktraders.paybacktraders.activity.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.AddClientActivity
import com.paybacktraders.paybacktraders.activity.UpdateProductDetailsActivity
import com.paybacktraders.paybacktraders.adapters.ClientAdapter
import com.paybacktraders.paybacktraders.adapters.ProductAdapter
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.ActivitySearchBinding
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

class SearchActivity : AppCompatActivity() {
    lateinit var binding:ActivitySearchBinding
    lateinit var viewModel: MainViewModel
    var where=""
    var announcementAdapter = ClientAdapter()
    var productAdapter = ProductAdapter()


    private fun setUpViewModel() {
        val dispatchers: CoroutineDispatcher = Dispatchers.Main
        val mainRepos = DefaultMainRepositories() as MainRepos
        val fanxApi: Apis = ApiClient().service
        val viewModelProviderfactory =
            MainViewModelProvider(application, mainRepos, dispatchers, fanxApi)
        viewModel = ViewModelProvider(this, viewModelProviderfactory)[MainViewModel::class.java]
    }

    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null


    private fun setUpLoadingDialog(context: Context) {
        builderAlert = AlertDialog.Builder(context)
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
        alertDialog = builderAlert!!.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpLoadingDialog(this)
        setUpViewModel()
        where=intent.getStringExtra(Global.INTENT_WHERE).toString()
        Toasty.success(this,where).show()

        if (where.equals(Global.SEARCH_CLIENT,ignoreCase = true)){
            var hashMap=HashMap<String,Any>()
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            hashMap[Global.PAYLOAD_TYPE] = "All"
            hashMap[Global.PAYLOAD_FROM_DATE] = ""
            hashMap[Global.PAYLOAD_TO_DATE] = ""

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
            setUpRecyclerView()
            announcementAdapter.setOnAttachmentClickListener {
                if (it.PaymentProof.isEmpty()){
                    Toasty.info(this,"NO Data Found").show()
                }else{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Global.PDF_BASE_URL+it.PaymentProof))

                    // Check if there's any app that can handle the Intent (e.g., Chrome)

                    // Open the link in the Chrome browser
                    startActivity(intent)
                }

            }

//            announcementAdapter.setOnRemarkClickListener {
//                val bundle=Bundle().apply {
//                    putString("id",it.id.toString())
//                }
//                findNavController().navigate(R.id.dialogRemarkStatusBinding,bundle)
//            }
            announcementAdapter.setOnItemClickListener {
                if (it.ConnectionStatus.equals("Connect", ignoreCase = true)) {
                    Toasty.success(this, "Already Connected", Toasty.LENGTH_SHORT).show()
                } else {
                    if (Prefs.getString(Global.Role).equals(Global.ADMIN_STRING)) {
                        val alert = AlertDialog.Builder(this)
                        alert.setTitle("Are you sure...You want to change the status?")
                        alert.setPositiveButton(
                            "yes",
                            DialogInterface.OnClickListener { dialog, which ->
                                viewModel.updateCustomerStatus(
                                    BodyClientStatus(
                                        EmployeeId = Prefs.getInt(Global.ID),
                                        CustomerId = it.id,
                                        Status = "Approved",
                                        Remarks = "${it.FullName} approved by ${Prefs.getString(Global.FullName)}"
                                    )
                                )
                                SubscribeToStatus()

                            })
                        alert.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                            //  Toasty.warning(requireContext(), "NO", Toasty.LENGTH_SHORT).show()

                        })
                        alert.show()

                    } else {
                        Toasty.warning(this, "Not Authorized", Toasty.LENGTH_SHORT).show()
                    }
                }


            }
        }
        else if (where.equals(Global.SEARCH_PRODUCT,ignoreCase = true)){
            var hashMap=HashMap<String,Any>()
            if (Prefs.getString(Global.Role).equals("admin", ignoreCase = true)) {
                viewModel.getProductAll()
                setUpRecyclerView()
                subscribeToProductAllObserver()
            } else {
                hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                viewModel.getProductAllFilter(hashMap)
                subscribeToFilterObserver()
                setUpRecyclerView()
            }


            productAdapter.setOnAddClientItemClickListener{ data->
                Intent(this, AddClientActivity::class.java).also {
                    it.putExtra(Global.ID,data.id)
                    startActivity(it)
                }
            }

            productAdapter.setOnFullItemClickListener { dataProduct ->
                val bundle=Bundle().apply {
                    putSerializable("product",dataProduct)
                }
                Intent(this, UpdateProductDetailsActivity::class.java).also {
                    // it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                    it.putExtra("product",dataProduct)
                    startActivity(it)
                }
            }

            productAdapter.setOnItemClickListener {
                /*Create an ACTION_SEND Intent*/
                /*Create an ACTION_SEND Intent*/
                val intent = Intent(Intent.ACTION_SEND)
                /*This will be the actual content you wish you share.*/
                /*This will be the actual content you wish you share.*/
                val shareBody =
                    "http://paybackbytrades.com/assets/html/form.html?id=${Prefs.getInt(Global.ID)}&ProductID=${it.id}"

                /*The type of the content is text, obviously.*/
                /*The type of the content is text, obviously.*/intent.type = "text/plain"
                /*Applying information Subject and Body.*/
                /*Applying information Subject and Body.*/intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "http://paybackbytrades.com/assets/html/form.html?id=${Prefs.getInt(Global.ID)}&ProductID=${it.id}"
                //  getString()
            )
                intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                /*Fire!*/
                /*Fire!*/startActivity(Intent.createChooser(intent, "Share Via"))
            }
        }

    }



    private fun setUpRecyclerView() = binding?.rvSearchResult?.apply {
        adapter = announcementAdapter
        layoutManager = LinearLayoutManager(this@SearchActivity)
    }



    private fun SubscribeToStatus() {
        viewModel.updateCustomerStatus.observe(this, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(this, it).show()
               // Log.e(ClientFragment.TAG, "SubscribeToStatus:$it ")
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
                        announcementAdapter.announcement = it.data

//                        if (brokerName.isEmpty()){
//
//                            announcementAdapter.announcement = it.data
//                        }else{
//
//                            var masterList = mutableListOf<DataCLient>()
//
//                            for (master in it.data) {
//                                if (master.BrokerName == brokerName) {
//
//                                    masterList.add(master)
//                                }
//                            }
//                            if (masterList.isEmpty()){
//                                binding!!.nodataFound.visibility = View.VISIBLE
//                            }else{
//                                binding!!.nodataFound.visibility = View.GONE
//                            }
//
//                        }

                    }


                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }


    private fun subscribeToProductAllObserver() {


        viewModel.productAll.observe(this, Event.EventObserver(
            onError = {
                alertDialog!!.hide()
                Toasty.error(this, it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.hide()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        productAdapter.product = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        productAdapter.product = it.data
                    }


                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }

    private fun subscribeToProductFIlterObserver() {


        viewModel.productAll.observe(this, Event.EventObserver(
            onError = {
                alertDialog!!.hide()
                Toasty.error(this, it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.hide()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        productAdapter.product = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        productAdapter.product = it.data
                    }


                } else {
                    Toasty.error(this, it.message).show()
                }

            }
        ))
    }


}