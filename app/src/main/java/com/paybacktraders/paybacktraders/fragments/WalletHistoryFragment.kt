package com.paybacktraders.paybacktraders.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.WalletHistoryAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentWalletHistoryBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataProduct
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataWalletHistory
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
 * Use the [WalletHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletHistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var startDate=""
    var endDate=""

    var _binding: FragmentWalletHistoryBinding? = null
    val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var walletHistoryAdapter = WalletHistoryAdapter()
    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null
    private var itemList: MutableList<DataWalletHistory> = mutableListOf()

    private fun filterList(query: String) {
        val filteredList = itemList.filter { item ->
            item.CustomerName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        walletHistoryAdapter.filterData(filteredList)
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
        item.isVisible = false
        itemDate.isVisible = true
        itemSearch.isVisible = true
        itemFilter.isVisible=false
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
                    startDate=Global.formatDateFromMilliseconds(it.first)
                    endDate=Global.formatDateFromMilliseconds(it.second)
                    var hashMap = HashMap<String, Any>()
                    hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                    hashMap[Global.PAYLOAD_FROM_DATE] = startDate
                    hashMap[Global.PAYLOAD_TO_DATE] = endDate
                    viewModel.getWalletHistory(hashMap)
                    subsribeToObserver()
                    setUprecyclerVIew()

                    //   Timber.i("The selected date range is ${it.first} - ${it.second}")

                }
                return true
            }
            R.id.searchMenu -> {
                //   Toasty.info(requireContext(),"Date").show()
                Intent(requireActivity(), SearchActivity::class.java).also {
                    it.putExtra(Global.INTENT_WHERE, Global.SEARCH_WALLET_HISTORY)
                    startActivity(it)

                }
                return true
            }


        }


        return super.onOptionsItemSelected(item)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet_history, container, false)
    }

    companion object {
        private const val TAG = "WalletHistoryFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NavigationDrawerActivity).viewModel
        _binding = FragmentWalletHistoryBinding.bind(view)
        setUpLoadingDialog(requireContext())
        var hashMap = HashMap<String, Any>()
        hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
        hashMap[Global.PAYLOAD_FROM_DATE] = startDate
        hashMap[Global.PAYLOAD_TO_DATE] = endDate
        viewModel.getWalletHistory(hashMap)
        subsribeToObserver()
        setUprecyclerVIew()

        var mapp=kotlin.collections.HashMap<String,Any>()
        mapp[Global.PAYLOAD_ID] = Prefs.getInt(Global.ID)
        viewModel.getDashboardData(mapp)
        SubscribeTOWallet()

    }


    private fun SubscribeTOWallet(){
        viewModel.dashBOardData.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                //  Global.hideDialog()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                // Global.showDialog(requireActivity())

            }, {
                //  Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    binding?.apply {

                        binding!!.tvWalletAmount.text = "${'$'} ${it.data[0].totalAmountInWallet.toString()}"
                    }

                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

    private fun setUprecyclerVIew() = binding!!.rvWalletHistory.apply {
        adapter = walletHistoryAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subsribeToObserver() {
        viewModel.walletHistoryData.observe(viewLifecycleOwner, Event.EventObserver(
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
                        walletHistoryAdapter.masterDistributor =
                            it.data as MutableList<DataWalletHistory>

                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        itemList.clear()
                        itemList.addAll(it.data)
                        walletHistoryAdapter.masterDistributor =
                            it.data as MutableList<DataWalletHistory>
                    }

                } else {
                    Toasty.error(requireContext(), it.message).show()
                }
            }
        ))
    }
}