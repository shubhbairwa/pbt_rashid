package com.paybacktraders.paybacktraders.activity.ui.gallery


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.activity.ui.AddProductActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.ContactUsAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentEnquiryBinding
import com.paybacktraders.paybacktraders.global.Global

import com.paybacktraders.paybacktraders.model.model.apiresponse.DataContactUs
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataWalletHistory
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import java.util.*


class EnquiryFragment : Fragment() {

        private var _binding: FragmentEnquiryBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        lateinit var viewModel: MainViewModel
        var contactUsAdapter = ContactUsAdapter()

        var alertDialog: AlertDialog? = null
        var builderAlert: AlertDialog.Builder? = null
        private var itemList: MutableList<DataContactUs> = mutableListOf()

        private fun filterList(query: String) {
            val filteredList = itemList.filter { item ->
                item.Name.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
            }

            contactUsAdapter.filterData(filteredList)
        }


        private fun setUpLoadingDialog(context: Context) {
            builderAlert = AlertDialog.Builder(context)
            builderAlert!!.setView(R.layout.dialog_alert)
            builderAlert!!.setCancelable(false)
            alertDialog = builderAlert!!.create()
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)


            _binding = FragmentEnquiryBinding.inflate(inflater, container, false)
            val root: View = binding.root

            //   val textView: TextView = binding.textGallery
//        galleryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
            return root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
        }


        override fun onPrepareOptionsMenu(menu: Menu) {
            val item: MenuItem = menu.findItem(R.id.action_settings)
            val itemSearch: MenuItem = menu.findItem(R.id.searchMenu)
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
            itemSearch.isVisible=true
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
                    //   Toasty.info(requireContext(),"Date").show()
                    Intent(requireActivity(), SearchActivity::class.java).also {
                        it.putExtra(Global.INTENT_WHERE, Global.SEARCH_ENQUIRY)
                        startActivity(it)

                    }
                    return true
                }
            }


            return super.onOptionsItemSelected(item)


        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
            viewModel = (activity as NavigationDrawerActivity).viewModel
            _binding = FragmentEnquiryBinding.bind(view)
            setUpLoadingDialog(requireContext())
            viewModel.getcontactusall()
            subscribeToObserver()
            setUpRecyclerView()

            contactUsAdapter.setOnItemClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.MobileNo}"))
                startActivity(intent)

            }

            contactUsAdapter.setOnMailItemClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(it.EmailId))
                }
                startActivity(intent)
            }


        }



        private fun setUpRecyclerView() = binding.rvContactUs.apply {
            adapter = contactUsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        private fun subscribeToObserver() {
            viewModel.contactUsAll.observe(viewLifecycleOwner, Event.EventObserver(
                onError = {
                    alertDialog!!.dismiss()
                    Toasty.error(requireContext(), it, Toasty.LENGTH_SHORT).show()
                }, onLoading = {
                    alertDialog!!.show()
                }, {
                    alertDialog!!.dismiss()
                    if (it.status.equals(200)) {
                        if (it.data.isEmpty()) {
                            binding.nodataFound.visibility = View.VISIBLE
                        } else {
                            binding.nodataFound.visibility = View.GONE
                            itemList.clear()
                            itemList.addAll(it.data)
                            contactUsAdapter.product = it.data as MutableList<DataContactUs>
                        }
                    } else {
                        Toasty.error(requireContext(), it.message, Toasty.LENGTH_SHORT).show()
                    }
                }
            ))
        }


    }


