package com.paybacktraders.paybacktraders.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.activity.SendWithDrawAmountActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.WalletHistoryAdapter
import com.paybacktraders.paybacktraders.adapters.WalletWithdrawlAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentWalletHistoryBinding
import com.paybacktraders.paybacktraders.databinding.FragmentWalletWithdrawlBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataWalletHistory
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataWithdrawlRequest
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WalletWithdrawlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletWithdrawlFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var startDate = ""
    var endDate = ""

    var statusList = arrayListOf<String>("All", "Pending", "Approved", "Rejected")
    var statusName = "All"

    var _binding: FragmentWalletWithdrawlBinding? = null
    val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var walletWithdrawlAdapter = WalletWithdrawlAdapter()
    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null
    private var itemList: MutableList<DataWithdrawlRequest> = mutableListOf()

    private fun filterList(query: String) {
        val filteredList = itemList.filter { item ->
            item.EmployeeName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        walletWithdrawlAdapter.filterData(filteredList)
    }


    private fun setUpLoadingDialog(context: Context) {
        builderAlert = AlertDialog.Builder(context)
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
        alertDialog = builderAlert!!.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        setHasOptionsMenu(true)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
        val itemDate: MenuItem = menu.findItem(R.id.dateMenu)
        val itemFilter: MenuItem = menu.findItem(R.id.filterMenu)
        item.isVisible = !Prefs.getString(Global.Role).equals("Admin", ignoreCase = true)
        val searchView = itemSearch?.actionView as SearchView

        // Set up the SearchView's query text listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Filter the adapter based on the search query
                // masterDistributorAdapter.filter.filter(newText)
                //  Log.e(MasterDistributorFragment.TAG, "onQueryTextChange: $newText", )
                filterList(newText)
                return true
            }
        })

        itemDate.isVisible = true
        itemSearch.isVisible = true
        itemFilter.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dateMenu -> {
                val builder = MaterialDatePicker.Builder.dateRangePicker()
                val now = Calendar.getInstance()
                builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

                val picker = builder.build()
                picker.show(activity?.supportFragmentManager!!, picker.toString())

                picker.addOnNegativeButtonClickListener { picker.dismiss() }
                picker.addOnPositiveButtonClickListener {
                    startDate = Global.formatDateFromMilliseconds(it.first)
                    endDate = Global.formatDateFromMilliseconds(it.second)
                    var hashMap = HashMap<String, Any>()
                    hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                    hashMap[Global.PAYLOAD_FROM_DATE] = startDate
                    hashMap[Global.PAYLOAD_TO_DATE] = endDate
                    hashMap[Global.PAYLOAD_TYPE] = statusName
                    viewModel.getWithdrawlRequestList(hashMap)
                    subsribeToObserver()
                    setUprecyclerVIew()

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
                Intent(requireActivity(), SearchActivity::class.java).also {
                    it.putExtra(Global.INTENT_WHERE, Global.SEARCH_withdrawl)
                    startActivity(it)

                }
                return true
            }

            R.id.action_settings -> {
                //   Toasty.info(requireContext(),"Date").show()
                Intent(requireActivity(), SendWithDrawAmountActivity::class.java).also {
                    // it.putExtra(Global.INTENT_WHERE, Global.SEARCH_withdrawl)
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
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                statusList
            )
        binding!!.acStatus.setAdapter<ArrayAdapter<String>>(adapter)

        binding!!.acStatus.setOnItemClickListener { parent, view, position, id ->
            statusName = statusList[position]
            var hashMap = HashMap<String, Any>()
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            hashMap[Global.PAYLOAD_FROM_DATE] = startDate
            hashMap[Global.PAYLOAD_TO_DATE] = endDate
            hashMap[Global.PAYLOAD_TYPE] = statusName
            viewModel.getWithdrawlRequestList(hashMap)
            subsribeToObserver()
            setUprecyclerVIew()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet_withdrawl, container, false)
    }

    companion object {
        private const val TAG = "WalletWithdrawlFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletWithdrawlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletWithdrawlFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NavigationDrawerActivity).viewModel
        _binding = FragmentWalletWithdrawlBinding.bind(view)
        setUpLoadingDialog(requireContext())

        setupStatus()
        var hashMap = HashMap<String, Any>()
        hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
        hashMap[Global.PAYLOAD_FROM_DATE] = startDate
        hashMap[Global.PAYLOAD_TO_DATE] = endDate
        hashMap[Global.PAYLOAD_TYPE] = statusName
        viewModel.getWithdrawlRequestList(hashMap)
        subsribeToObserver()
        setUprecyclerVIew()

        walletWithdrawlAdapter.setOnItemClickListener {
            if (it.Status.equals("Approved", ignoreCase = true)) {
                Toasty.success(requireContext(), "Already Connected", Toasty.LENGTH_SHORT).show()
            }else if(it.Status.equals("Rejected", ignoreCase = true)){
                Toasty.success(requireContext(), "Already Rejected", Toasty.LENGTH_SHORT).show()
            }

            else {
                if (Prefs.getString(Global.Role).equals(Global.ADMIN_STRING)) {
                    val alert = AlertDialog.Builder(requireContext())
                    alert.setTitle("Are you sure...You want to change the status?")
                    alert.setPositiveButton(
                        "Approved",
                        DialogInterface.OnClickListener { dialog, which ->
                            var hashMap = kotlin.collections.HashMap<String, Any>()
                            hashMap["RequestId"] = it.id
                            hashMap["EmployeeId"] = Prefs.getInt(Global.ID)
                            hashMap["Status"] = "Approved"
                            hashMap["Remarks2"] = "Your request has been approved"

                            viewModel.withdrawlApproval(
                                hashMap
                            )
                            SubscribeToStatus()

                        })
                    alert.setNegativeButton("Rejected", DialogInterface.OnClickListener { dialog, which ->
                        //  Toasty.warning(requireContext(), "NO", Toasty.LENGTH_SHORT).show()
                        var hashMap = kotlin.collections.HashMap<String, Any>()
                        hashMap["RequestId"] = it.id
                        hashMap["EmployeeId"] = Prefs.getInt(Global.ID)
                        hashMap["Status"] = "Rejected"
                        hashMap["Remarks2"] = "Your request has been Rejected"

                        viewModel.withdrawlApproval(
                            hashMap
                        )
                        SubscribeToStatus()
                    })
                    alert.show()

                } else {
                    Toasty.warning(requireContext(), "Not Authorized", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUprecyclerVIew() = binding!!.rvWalletWithdrawl.apply {
        adapter = walletWithdrawlAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun SubscribeToStatus(){
        viewModel.walletApproval.observe(viewLifecycleOwner,Event.EventObserver(
            onError = {
                alertDialog!!.dismiss()
                Toasty.error(requireContext(),it).show()
            }, onLoading = {
                alertDialog!!.show()
            },{
                alertDialog!!.dismiss()
                if (it.status.equals(200)){
                    Toasty.success(requireContext(),it.message).show()
                    var hashMap = HashMap<String, Any>()
                    hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                    hashMap[Global.PAYLOAD_FROM_DATE] = startDate
                    hashMap[Global.PAYLOAD_TO_DATE] = endDate
                    hashMap[Global.PAYLOAD_TYPE] = statusName
                    viewModel.getWithdrawlRequestList(hashMap)
                    subsribeToObserver()
                    setUprecyclerVIew()
                }else{
                    Toasty.error(requireContext(),it.message).show()
                }
            }
        ))
    }

    private fun subsribeToObserver() {
        viewModel.walletWithdrwalListRequestData.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                alertDialog!!.dismiss()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                alertDialog!!.show()
            }, {
                alertDialog!!.dismiss()
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        walletWithdrawlAdapter.masterDistributor =
                            it.data as MutableList<DataWithdrawlRequest>

                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        itemList.clear()
                        itemList.addAll(it.data)
                        walletWithdrawlAdapter.masterDistributor =
                            it.data as MutableList<DataWithdrawlRequest>
                    }

                }
                else if (it.status.equals(201)){
                    alertDialog?.dismiss()
                    Toasty.error(requireContext(), it.message).show()
                }
                else {
                    alertDialog?.dismiss()
                    Toasty.error(requireContext(), it.message).show()
                }
            }
        ))
    }

}