package com.paybacktraders.paybacktraders.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.AdminActivity
import com.paybacktraders.paybacktraders.activity.MasterDistributorActivity
import com.paybacktraders.paybacktraders.adapters.ClientAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentDashBoardBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import com.pixplicity.easyprefs.library.Prefs
import es.dmoral.toasty.Toasty

class DashBoardFragment : Fragment(R.layout.fragment_dash_board) {
    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var announcementAdapter = ClientAdapter()


    companion object {
        private const val TAG = "DashBoardFragment"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        where = activity?.intent?.getStringExtra(Global.INTENT_WHERE).toString()
        Log.e(TAG, "onViewCreated: $where")

        /***check for type of user so that we assign viewmodel according to their corresponding activity**/
        viewModel = if (where.equals("admin", ignoreCase = true)) {
            Log.e(TAG, "onViewCreated: utut")
            (activity as AdminActivity).viewModel
        } else {
            Log.e(TAG, "onViewCreated: master")
            (activity as MasterDistributorActivity).viewModel
        }
        _binding = FragmentDashBoardBinding.bind(view)


        hashMap[Global.PAYLOAD_ID] = Prefs.getInt(Global.ID)
        viewModel.getDashboardData(hashMap)
        viewModel.getClientAll()
        subscribeToObserver()
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

    }

    private fun setUpRecyclerView() = binding?.recyclerView?.apply {
        adapter = announcementAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun SubscribeToStatus() {
        viewModel.updateCustomerStatus.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Global.hideDialog()
                Toasty.error(requireContext(), it).show()
                Log.e(TAG, "SubscribeToStatus:$it ",)
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

    private fun subscribeToObserver() {
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
                        tvClientCounter.text = it.data[0].totalCustomer.toString()
                        tvDistributorCounter.text = it.data[0].totalDistributor.toString()
                        tvProductCounter.text = it.data[0].totalProduct.toString()
                        tvWalletCounter.text = "USD ${it.data[0].totalAmountInWallet.toString()}"
                    }

                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))


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

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ")
//        hashMap["id"] = Prefs.getInt(Global.ID)
//        viewModel.getDashboardData(hashMap)
//        subscribeToObserver()

    }
}