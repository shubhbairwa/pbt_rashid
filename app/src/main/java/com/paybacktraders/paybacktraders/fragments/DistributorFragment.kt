package com.paybacktraders.paybacktraders.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.*
import com.paybacktraders.paybacktraders.activity.ui.AddProductActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.MasterDistributorAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentDistributorBinding
import com.paybacktraders.paybacktraders.databinding.FragmentMasterDistributorBinding
import com.paybacktraders.paybacktraders.databinding.FragmentProductBinding
import com.paybacktraders.paybacktraders.global.Global
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
 * Use the [DistributorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DistributorFragment : Fragment() {
    var _binding: FragmentDistributorBinding? = null
    val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var masterDistributorAdapter = MasterDistributorAdapter()
    private var itemList: MutableList<DataEmployeeAll> = mutableListOf()
    var alertDialog: AlertDialog?=null
    var builderAlert: AlertDialog.Builder?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distributor, container, false)
    }

    private fun filterList(query: String) {
        val filteredList = itemList.filter { item ->
            item.FullName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        masterDistributorAdapter.filterData(filteredList)
    }


    companion object {
        private const val TAG = "DistributorFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DistributorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DistributorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
        val searchView = itemSearch?.actionView as SearchView


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

        item.isVisible = false
        itemSearch.isVisible = true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Intent(activity, AddProductActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }

            R.id.searchMenu -> {
                Intent(activity, SearchActivity::class.java).also {
                    it.putExtra(Global.INTENT_WHERE,Global.SEARCH_DIST)
                    startActivity(it)
                }
                return true
            }
        }


        return super.onOptionsItemSelected(item)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        where = Prefs.getString(Global.INTENT_WHERE)
        Log.e(DistributorFragment.TAG, "onViewCreated: $where")

        /***check for type of user so that we assign viewmodel according to their corresponding activity**/
        viewModel = if (where.equals("admin", ignoreCase = true)) {
            Log.e(DistributorFragment.TAG, "onViewCreated: utut")
            (activity as NavigationDrawerActivity).viewModel
        } else {
            Log.e(DistributorFragment.TAG, "onViewCreated: master")
            (activity as NavigationDrawerActivity).viewModel
        }
        _binding = FragmentDistributorBinding.bind(view)

        builderAlert=AlertDialog.Builder(requireContext())
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
        alertDialog=  builderAlert!!.create()






        binding!!.addMasterDistributor.setOnClickListener {
            Intent(activity, AddDistributorActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                startActivity(it)
            }
        }

        masterDistributorAdapter.setOnItemClickListener {data->
//            val bundle=Bundle().apply {
//                putSerializable("dist",data)
//            }
//            Intent(activity,UpdateDistributorDetailsActivity::class.java).also {
//                it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
//                it.putExtra("dist",data)
//                startActivity(it)
//            }

            val bundle = Bundle().apply {
                putSerializable("dist", data)
            }

            Intent(activity, DistributorDetailsActivity::class.java).also {
                //   it.putExtra(Global.INTENT_WHERE, Global.MASTER_DIST_STRING)
                it.putExtra("dist", data)
                startActivity(it)
            }
        }

        masterDistributorAdapter.setOnMenuItemClickListener { dataEmployeeAll, masterDistributionItemListLayoutBinding ->
            //  masterDistributorAdapter.sho(masterDistributionItemListLayoutBinding.ivMenuPopUp)
            showPopupMenuRecycler(masterDistributionItemListLayoutBinding.ivMenuPopUp,dataEmployeeAll)
        }

    }



    private fun setUpRecyclerView() = binding?.rvDistributor?.apply {
        adapter = masterDistributorAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun subscribeToObserver() {


        viewModel.employeeAll.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                alertDialog!!.dismiss()
               // Global.hideDialog()
                Toasty.error(requireContext(), it).show()
            }, onLoading = {
                           alertDialog!!.show()
               // Global.showDialog(requireActivity())

            }, {
                alertDialog!!.dismiss()
              //  Global.hideDialog()
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
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }


    private fun showPopupMenuRecycler(view: View,data: DataEmployeeAll) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.distributor_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editDist -> {
                    Intent(activity, UpdateDistributorDetailsActivity::class.java).also {
                        if (data.Role.equals(Global.DISTRIBUTOR_STRING,ignoreCase = true)){
                            it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                        }else{
                            it.putExtra(Global.INTENT_WHERE, Global.MASTER_DIST_STRING)
                        }

                        //it.putExtra(Global.INTENT_EDIT_WHERE, Global.MASTER_EDIT_DIST_STRING)
                        it.putExtra("dist", data)
                        startActivity(it)
                    }
                    // Handle option 1 click
                    true
                }
                R.id.menuViewDistributors -> {
                    // Handle option 2 click
                    Intent(activity, ListOfDistributorActivity::class.java).also {
                        it.putExtra(Global.ID, data.id)
                        startActivity(it)
                    }
                    true
                }
                R.id.menuViewClients -> {
                    // Handle option 3 click

                    Intent(activity, ListOfCustomerDetailActivity::class.java).also {
                        it.putExtra(Global.ID, data.id)
                        startActivity(it)
                    }
                    true
                }
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }


    override fun onResume() {
        super.onResume()
        hashMap[Global.PAYLOAD_ID] = Prefs.getInt(Global.ID)
        // viewModel.getDashboardData(hashMap)
        viewModel.getDistributor(hashMap)
        subscribeToObserver()
        setUpRecyclerView()
    }

}