package com.paybacktraders.paybacktraders.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.AdminActivity
import com.paybacktraders.paybacktraders.activity.MasterDistributorActivity
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.adapters.ClientAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentClientBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
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
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : Fragment() {
    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var announcementAdapter = ClientAdapter()


    var startDate=""
    var endDate=""

    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null
    var brokerList = arrayListOf<String>()
    var brokerName = ""

    var statusList = arrayListOf<String>("All","Pending","Approved","Rejected","Connect","Disconnect")
    var statusName = ""


    private fun setUpLoadingDialog(context: Context) {
        builderAlert = AlertDialog.Builder(context)
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
        alertDialog = builderAlert!!.create()
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    companion object {
        private const val TAG = "ClientFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
        val itemDate: MenuItem = menu.findItem(R.id.dateMenu)
        val itemFilter: MenuItem = menu.findItem(R.id.filterMenu)
        item.isVisible = false
        itemDate.isVisible = true
        itemSearch.isVisible = true
        itemFilter.isVisible=true
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
                    hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
                    hashMap[Global.PAYLOAD_TYPE] = "Connect"
                    hashMap[Global.PAYLOAD_FROM_DATE] = startDate
                    hashMap[Global.PAYLOAD_TO_DATE] = endDate

                    viewModel.getClientALlFilter(hashMap)
                    subscribeToFilterObserver()

                 //   Timber.i("The selected date range is ${it.first} - ${it.second}")

                }
                return true
            }

            R.id.filterMenu -> {
                Log.e(TAG, "onOptionsItemSelected: ", )
                binding!!.apply {

                        if (linearFilters.visibility==View.GONE){
                            linearFilters.visibility=View.VISIBLE
                        }else{
                            linearFilters.visibility=View.GONE
                        }

                }



                return true
            }
        }


        return super.onOptionsItemSelected(item)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        where = Prefs.getString(Global.INTENT_WHERE)
        Log.e(TAG, "onViewCreated: $where")

        /***check for type of user so that we assign viewmodel according to their corresponding activity**/
        viewModel = if (where.equals("admin", ignoreCase = true)) {
            Log.e(TAG, "onViewCreated: utut")
            (activity as NavigationDrawerActivity).viewModel
        } else {
            Log.e(TAG, "onViewCreated: master")
            (activity as NavigationDrawerActivity).viewModel
        }
        _binding = FragmentClientBinding.bind(view)
        setUpLoadingDialog(requireContext())



        if (where.equals("admin", ignoreCase = true)) {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            hashMap[Global.PAYLOAD_TYPE] = "Connect"
            hashMap[Global.PAYLOAD_FROM_DATE] = ""
            hashMap[Global.PAYLOAD_TO_DATE] = ""

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
        } else {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            hashMap[Global.PAYLOAD_TYPE] = "All"
            hashMap[Global.PAYLOAD_FROM_DATE] = ""
            hashMap[Global.PAYLOAD_TO_DATE] = ""

            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
        }

        binding!!.linearFilterButton.setOnClickListener {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            hashMap[Global.PAYLOAD_TYPE] = statusName
            hashMap[Global.PAYLOAD_FROM_DATE] = startDate
            hashMap[Global.PAYLOAD_TO_DATE] = endDate
            viewModel.getClientALlFilter(hashMap)
            subscribeToFilterObserver()
        }

        announcementAdapter.setOnAttachmentClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.PaymentProof))

            // Check if there's any app that can handle the Intent (e.g., Chrome)

                // Open the link in the Chrome browser
                startActivity(intent)

        }

        announcementAdapter.setOnRemarkClickListener {
            val bundle=Bundle().apply {
                putString("id",it.id.toString())
            }
            findNavController().navigate(R.id.dialogRemarkStatusBinding,bundle)
        }



        viewModel.getBrokerAll()
        subscribeToBrokerObserver()

        // viewModel.getDashboardData(hashMap)

        setUpRecyclerView()


        announcementAdapter.setOnItemClickListener {
            if (it.ConnectionStatus.equals("Connect", ignoreCase = true)) {
                Toasty.success(requireContext(), "Already Connected", Toasty.LENGTH_SHORT).show()
            } else {
                if (Prefs.getString(Global.Role).equals(Global.ADMIN_STRING)) {
                    val alert = AlertDialog.Builder(requireContext())
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
                    Toasty.warning(requireContext(), "Not Authorized", Toasty.LENGTH_SHORT).show()
                }
            }


        }
        setupStatus()


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
        }
    }


    private fun subscribeToBrokerObserver() {
        viewModel.brokerAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
            }, onLoading = {

            }, {
                if (it.status.equals(200)) {
                    for (productName in it.data) {
                        brokerList.add(productName.BrokerName)
                    }
                    val adapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            brokerList
                        )
                    binding!!.acBrokerName.setAdapter<ArrayAdapter<String>>(adapter)

                    binding!!.acBrokerName.setOnItemClickListener { parent, view, position, id ->
                        brokerName = brokerList[position]
                    }
                } else {
                    Toasty.error(requireContext(), it.message, Toasty.LENGTH_SHORT).show()
                }
            }
        ))
    }

    private fun SubscribeToStatus() {
        viewModel.updateCustomerStatus.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireContext(), it).show()
                Log.e(ClientFragment.TAG, "SubscribeToStatus:$it ")
            }, onLoading = {
                Global.showDialog(requireActivity())

            }, {
                Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    Toasty.success(requireContext(), it.message, Toasty.LENGTH_SHORT).show()

                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

    private fun setUpRecyclerView() = binding?.rvClientFragment?.apply {
        adapter = announcementAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun subscribeToObserver() {


        viewModel.clientAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                Global.showDialog(requireActivity())

            }, {
                Global.hideDialog()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        announcementAdapter.announcement = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

    private fun subscribeToFilterObserver() {


        viewModel.clientFilter.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
               alertDialog!!.dismiss()
                Toasty.error(requireContext(), it).show()
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
                        if (brokerName.isEmpty()){

                            announcementAdapter.announcement = it.data
                        }else{

                            var masterList = mutableListOf<DataCLient>()

                            for (master in it.data) {
                                if (master.BrokerName == brokerName) {

                                    masterList.add(master)
                                }
                            }
                            if (masterList.isEmpty()){
                                binding!!.nodataFound.visibility = View.VISIBLE
                            }else{
                                binding!!.nodataFound.visibility = View.GONE
                            }
                            announcementAdapter.announcement = masterList
                        }

                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }


}