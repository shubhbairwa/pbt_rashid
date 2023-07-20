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
import com.paybacktraders.paybacktraders.databinding.FragmentDistributorBinding
import com.paybacktraders.paybacktraders.databinding.FragmentMasterDistributorBinding
import com.paybacktraders.paybacktraders.databinding.FragmentProductBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distributor, container, false)
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


        hashMap[Global.PAYLOAD_ID] = Prefs.getInt(Global.ID)
        // viewModel.getDashboardData(hashMap)
        viewModel.getDistributor(hashMap)
        subscribeToObserver()
        setUpRecyclerView()
        binding!!.addMasterDistributor.setOnClickListener {
            Intent(activity, AddDistributorActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                startActivity(it)
            }
        }

        masterDistributorAdapter.setOnItemClickListener {data->
            val bundle=Bundle().apply {
                putSerializable("dist",data)
            }
            Intent(activity,UpdateDistributorDetailsActivity::class.java).also {
                it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                it.putExtra("dist",data)
                startActivity(it)
            }
        }

    }

    private fun setUpRecyclerView() = binding?.rvDistributor?.apply {
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
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        masterDistributorAdapter.masterDistributor = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }


}