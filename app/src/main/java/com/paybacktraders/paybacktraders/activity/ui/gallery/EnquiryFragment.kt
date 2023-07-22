package com.paybacktraders.paybacktraders.activity.ui.gallery


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.NavigationDrawerActivity
import com.paybacktraders.paybacktraders.adapters.ContactUsAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentGalleryBinding
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataContactUs
import com.paybacktraders.paybacktraders.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty


class EnquiryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    =======
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import com.paybacktraders.paybacktraders.databinding.FragmentEnquiryBinding

    class EnquiryFragment : Fragment() {

        private var _binding: FragmentGalleryBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        lateinit var viewModel: MainViewModel
        var contactUsAdapter = ContactUsAdapter()

        var alertDialog: AlertDialog? = null
        var builderAlert: AlertDialog.Builder? = null


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


            _binding = FragmentGalleryBinding.inflate(inflater, container, false)
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
            item.isVisible = false
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = (activity as NavigationDrawerActivity).viewModel
            _binding = FragmentGalleryBinding.bind(view)
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
                            contactUsAdapter.product = it.data as MutableList<DataContactUs>
                        }
                    } else {
                        Toasty.error(requireContext(), it.message, Toasty.LENGTH_SHORT).show()
                    }
                }
            ))
        }


    }
}