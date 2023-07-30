package com.paybacktraders.paybacktraders.dialogs

import android.app.Dialog
import androidx.fragment.app.DialogFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.adapters.ClientStatusRemarkAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.DialogRemarkStatusBinding
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataClientStatusRemark
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty


class ClientStatusRemarkDialog : Fragment() {

    // ViewBinding instance
    private var _binding: DialogRemarkStatusBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel
    val args: ClientStatusRemarkDialogArgs by navArgs()
    var clientStatusRemarkAdapter = ClientStatusRemarkAdapter()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_remark_status, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NavigationDrawerActivity).viewModel
       _binding = DialogRemarkStatusBinding.bind(view)

        var hashMap = HashMap<String, Any>()
        hashMap["CustomerId"] = args.id
        viewModel.getCustomerStatus(hashMap)
        subscribeToObserver()
        setUpRecyclerView()


        // Now you can access and bind your views using the binding object
        //  binding.textViewDialogTitle.text = "Your Dialog Title"

//        binding.btnClose.setOnClickListener {
//            findNavController().navigateUp()
//            // Handle the negative button click here
//           // dismiss()
//        }
    }

    private fun setUpRecyclerView() = binding!!.recyclerViewRemarks.apply {
        adapter = clientStatusRemarkAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObserver() {
        viewModel.statusClientRemark.observe(viewLifecycleOwner, Event.EventObserver(
            onError = {
                Toasty.error(requireContext(), it).show()
                binding.progress.visibility=View.GONE
            }, onLoading = {
                binding.progress.visibility=View.VISIBLE
            }, {
                binding.progress.visibility=View.GONE
                if (it.status.equals(200)) {

                    if (it.data.isEmpty()) {
                        binding.noDataFound.visibility = View.VISIBLE
                    } else {
                        binding.noDataFound.visibility = View.GONE

                        clientStatusRemarkAdapter.masterDistributor =
                            it.data as MutableList<DataClientStatusRemark>
                    }
                } else {
                    Toasty.error(requireContext(), it.message).show()
                }
            }
        ))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
