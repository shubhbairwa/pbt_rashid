package com.paybacktraders.paybacktraders.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.*
import com.paybacktraders.paybacktraders.activity.ui.AddProductActivity
import com.paybacktraders.paybacktraders.adapters.MasterDistributorAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentMasterDistributorBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MasterDistributorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MasterDistributorFragment : Fragment() {
    var _binding: FragmentMasterDistributorBinding? = null
    val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var masterDistributorAdapter = MasterDistributorAdapter()

    var brokerList = arrayListOf<String>("All", "Master Distributor", "Distributor")
    var brokerName = ""

    var alertDialog: AlertDialog? = null
    var builderAlert: AlertDialog.Builder? = null


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


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        val itemCalendar: MenuItem = menu.findItem(R.id.dateMenu)
        val itemFilter: MenuItem = menu.findItem(R.id.filterMenu)
        item.isVisible = false
        itemCalendar.isVisible = false
        itemFilter.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_master_distributor, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Intent(activity, AddDistributorActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }

            R.id.dateMenu -> {
                Toasty.info(requireContext(), "Date").show()
                return true
            }

            R.id.filterMenu -> {
                //   Toasty.info(requireContext(),"Date").show()
                showPopupMenu(item.actionView!!.rootView)

                return true
            }
        }


        return super.onOptionsItemSelected(item)


    }

//    override fun onCreateContextMenu(
//        menu: ContextMenu,
//        v: View,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu!!, v!!, menuInfo)
//        requireActivity().menuInflater.inflate(R.menu.distributor_filter_floating_menu, menu)
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.all -> {
//
//                Toast.makeText(requireContext(), "color changed", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            R.id.distributor -> {
//
//                Toast.makeText(requireContext(), "size increased", Toast.LENGTH_SHORT).show()
//
//                return true
//            }
//            R.id.masterDistributor -> {
//
//                Toast.makeText(requireContext(), "size increased", Toast.LENGTH_SHORT).show()
//
//                return true
//            }
//            else -> {
//                return super.onContextItemSelected(item)
//            }
//        }
//    }


    private fun showPopupMenu(anchorview: View) {
//        val anchorView =
//            requireView().findViewById<View>(menuItem.itemId) // Get the view of the menu item


        val popupMenu = PopupMenu(requireContext(), anchorview)
        popupMenu.inflate(R.menu.distributor_filter_floating_menu)

        // Set a listener to handle menu item clicks
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.all -> {

                    Toast.makeText(requireContext(), "color changed", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.distributor -> {

                    Toast.makeText(requireContext(), "size increased", Toast.LENGTH_SHORT)
                        .show()

                    true
                }
                R.id.masterDistributor -> {

                    Toast.makeText(requireContext(), "size increased", Toast.LENGTH_SHORT)
                        .show()

                    true
                }
                else -> {
                    false
                }
            }
        }

        // Show the popup menu
        popupMenu.show()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        where = Prefs.getString(Global.INTENT_WHERE)
        Log.e(MasterDistributorFragment.TAG, "onViewCreated: $where")

        /***check for type of user so that we assign viewmodel according to their corresponding activity**/
        viewModel = if (where.equals("admin", ignoreCase = true)) {
            Log.e(MasterDistributorFragment.TAG, "onViewCreated: utut")
            (activity as NavigationDrawerActivity).viewModel
        } else {
            Log.e(MasterDistributorFragment.TAG, "onViewCreated: master")
            (activity as NavigationDrawerActivity).viewModel
        }
        _binding = FragmentMasterDistributorBinding.bind(view)
        setUpLoadingDialog(requireContext())

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                brokerList
            )
        binding!!.acTypeDistributor.setAdapter<ArrayAdapter<String>>(adapter)

        binding!!.acTypeDistributor.setOnItemClickListener { parent, view, position, id ->
            brokerName = brokerList[position]
            if (brokerName.equals("All", ignoreCase = true)) {
                viewModel.getEmployeeAll()
                subscribeToObserver()
            } else if (brokerName.equals("Master Distributor", ignoreCase = true)) {
                Log.e(TAG, "onViewCreated: $brokerName")
                viewModel.getEmployeeAll()
                subscribeToMasterObserver()
            } else {
                viewModel.getEmployeeAll()
                subscribeToDistributorObserver()
            }

        }


        viewModel.getEmployeeAll()
        setUpRecyclerView()
        subscribeToObserver()

        binding!!.addMasterDistributor.setOnClickListener {
            Intent(activity, AddDistributorActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE, Global.MASTER_DIST_STRING)
                startActivity(it)
            }
        }

        masterDistributorAdapter.setOnItemClickListener { data ->
            val bundle = Bundle().apply {
                putSerializable("dist", data)
            }
            Intent(activity, UpdateDistributorDetailsActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE, Global.MASTER_DIST_STRING)
                it.putExtra("dist", data)
                startActivity(it)
            }
        }

    }

    companion object {

        private const val TAG = "MasterDistributorFragme"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MasterDistributorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MasterDistributorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setUpRecyclerView() = binding?.rvMasterDistributor?.apply {
        adapter = masterDistributorAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.navigation_drawer, menu)
//        //  val distItem=menu.findItem(R.id.distributorFragment).setVisible(false)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> {
//                Intent(activity, AddProductActivity::class.java).also {
//                    startActivity(it)
//                }
//                return true
//            }
//        }
//
//
//        return super.onOptionsItemSelected(item)
//
//
//    }


    private fun subscribeToObserver() {


        viewModel.employeeAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                alertDialog!!.hide()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.hide()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        masterDistributorAdapter.masterDistributor = it.data

                        //  announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE

                        var masterList = mutableListOf<DataEmployeeAll>()

                        for (master in it.data) {
                            if (master.Role == "Master Distributor") {

                                masterList.add(master)
                            }
                        }
                        masterDistributorAdapter.masterDistributor = it.data


                        //  announcementAdapter.announcement = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

    private fun subscribeToMasterObserver() {


        viewModel.employeeAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                alertDialog!!.hide()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.hide()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        masterDistributorAdapter.masterDistributor = it.data

                        //  announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE

                        var masterList = mutableListOf<DataEmployeeAll>()

                        for (master in it.data) {
                            if (master.Role == "Master Distributor") {

                                masterList.add(master)
                            }
                        }
                        masterDistributorAdapter.masterDistributor = masterList


                        //  announcementAdapter.announcement = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }


    private fun subscribeToDistributorObserver() {


        viewModel.employeeAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                alertDialog!!.hide()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                alertDialog!!.show()

            }, {
                alertDialog!!.hide()
                //  Toasty.success(requireContext(),it)
                if (it.status.equals(200)) {
                    if (it.data.isEmpty()) {
                        binding!!.nodataFound.visibility = View.VISIBLE
                        masterDistributorAdapter.masterDistributor = it.data

                        //  announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE

                        var masterList = mutableListOf<DataEmployeeAll>()

                        for (master in it.data) {
                            if (master.Role == "Distributor") {

                                masterList.add(master)
                            }
                        }
                        masterDistributorAdapter.masterDistributor = masterList


                        //  announcementAdapter.announcement = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }


}