package com.paybacktraders.paybacktraders.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.*
import com.paybacktraders.paybacktraders.activity.ui.AddProductActivity
import com.paybacktraders.paybacktraders.activity.ui.SearchActivity
import com.paybacktraders.paybacktraders.adapters.ProductAdapter
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.databinding.FragmentProductBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataProduct
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
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding
    lateinit var viewModel: MainViewModel
    var where: String = ""
    var hashMap = HashMap<String, Any>()
    var productAdapter = ProductAdapter()
     var alertDialog: AlertDialog?=null
    var builderAlert:AlertDialog.Builder?=null
    private var itemList: MutableList<DataProduct> = mutableListOf()

    private fun filterList(query: String) {
        val filteredList = itemList.filter { item ->
            item.ProductName.toLowerCase(Locale.ENGLISH).contains(query.toLowerCase(Locale.ENGLISH))
        }

        productAdapter.filterData(filteredList)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            com.paybacktraders.paybacktraders.R.layout.fragment_product,
            container,
            false
        )
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
        item.isVisible = true
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
                    it.putExtra(Global.INTENT_WHERE, Global.SEARCH_PRODUCT)
                    startActivity(it)

                }
                return true
            }
        }


        return super.onOptionsItemSelected(item)


    }




    companion object {
        private const val TAG = "ProductFragment"


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        where = Prefs.getString(Global.INTENT_WHERE)
        Log.e(ProductFragment.TAG, "onViewCreated: $where")

        /***check for type of user so that we assign viewmodel according to their corresponding activity**/
        viewModel = if (where.equals("admin", ignoreCase = true)) {
            Log.e(ProductFragment.TAG, "onViewCreated: utut")
            (activity as NavigationDrawerActivity).viewModel
        } else {
            Log.e(ProductFragment.TAG, "onViewCreated: master")
            (activity as NavigationDrawerActivity).viewModel
        }
        _binding = FragmentProductBinding.bind(view)

        builderAlert=AlertDialog.Builder(requireContext())
        builderAlert!!.setView(R.layout.dialog_alert)
        builderAlert!!.setCancelable(false)
      alertDialog=  builderAlert!!.create()







        productAdapter.setOnAddClientItemClickListener{ data->
            Intent(requireActivity(), AddClientActivity::class.java).also {
                it.putExtra(Global.ID,data.id)
                startActivity(it)
            }
        }

        productAdapter.setOnFullItemClickListener { dataProduct ->
            val bundle=Bundle().apply {
                putSerializable("product",dataProduct)
            }
            Intent(activity, UpdateProductDetailsActivity::class.java).also {
               // it.putExtra(Global.INTENT_WHERE,Global.DISTRIBUTOR_STRING)
                it.putExtra("product",dataProduct)
                startActivity(it)
            }
        }

        productAdapter.setOnItemClickListener {
            /*Create an ACTION_SEND Intent*/
            /*Create an ACTION_SEND Intent*/
            val intent = Intent(Intent.ACTION_SEND)
            /*This will be the actual content you wish you share.*/
            /*This will be the actual content you wish you share.*/
            val shareBody =
                "http://paybackbytrades.com/assets/html/form.html?id=${Prefs.getInt(Global.ID)}&ProductID=${it.id}"

            /*The type of the content is text, obviously.*/
            /*The type of the content is text, obviously.*/intent.type = "text/plain"
            /*Applying information Subject and Body.*/
            /*Applying information Subject and Body.*/intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "http://paybackbytrades.com/assets/html/form.html?id=${Prefs.getInt(Global.ID)}&ProductID=${it.id}"
            //  getString()
        )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            /*Fire!*/
            /*Fire!*/startActivity(Intent.createChooser(intent, "Share Via"))
        }


    }

    override fun onResume() {
        super.onResume()
        if (where.equals("admin", ignoreCase = true)) {
            viewModel.getProductAll()
            subscribeToObserver()
        } else {
            hashMap[Global.PAYLOAD_EMPLOYEE_ID] = Prefs.getInt(Global.ID)
            viewModel.getProductAll()
            subscribeToObserver()
        }



        setUpRecyclerView()
    }


    private fun setUpRecyclerView() = binding?.rvProduct?.apply {
        adapter = productAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun subscribeToObserver() {


        viewModel.productAll.observe(viewLifecycleOwner, Event.EventObserver(
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
                        productAdapter.product = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        itemList.clear()
                        itemList.addAll(it.data)
                        productAdapter.product = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

    private fun subscribeToFIlterObserver() {


        viewModel.productAll.observe(viewLifecycleOwner, Event.EventObserver(
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
                        productAdapter.product = it.data
                    } else {
                        binding!!.nodataFound.visibility = View.GONE
                        productAdapter.product = it.data
                    }


                } else {
                    Toasty.error(requireContext(), it.message).show()
                }

            }
        ))
    }

}