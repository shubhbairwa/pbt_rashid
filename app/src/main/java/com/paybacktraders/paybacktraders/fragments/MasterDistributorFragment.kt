package com.paybacktraders.paybacktraders.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.*
import com.paybacktraders.paybacktraders.adapters.MasterDistributorAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentMasterDistributorBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty

class MasterDistributorFragment : Fragment() {
    var _binding: FragmentMasterDistributorBinding? = null
    val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var masterDistributorAdapter = MasterDistributorAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_master_distributor, container, false)
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


        viewModel.getEmployeeAll()
        setUpRecyclerView()
        subscribeToObserver()

        binding!!.addMasterDistributor.setOnClickListener {
            Intent(activity,AddDistributorActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE,Global.MASTER_DIST_STRING)
                startActivity(it)
            }
        }

        masterDistributorAdapter.setOnItemClickListener {data->
            val bundle=Bundle().apply {
                putSerializable("dist",data)
            }
            Intent(activity, UpdateDistributorDetailsActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE,Global.MASTER_DIST_STRING)
                it.putExtra("dist",data)
                startActivity(it)
            }
        }

    }

    companion object {
        private const val TAG = "MasterDistributorFragme"
    }

    private fun setUpRecyclerView() = binding?.rvMasterDistributor?.apply {
        adapter = masterDistributorAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun subscribeToObserver() {


        viewModel.employeeAll.observe(viewLifecycleOwner, Event.EventObserver(
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
                        masterDistributorAdapter.masterDistributor = it.data

                        //  announcementAdapter.announcement = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE

                        var masterList = mutableListOf<DataEmployeeAll>()

                        for (master in it.data) {
                            if (master.Role=="Master Distributor") {

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