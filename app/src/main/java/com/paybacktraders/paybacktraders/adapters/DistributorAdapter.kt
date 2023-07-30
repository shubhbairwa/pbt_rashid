package com.paybacktraders.paybacktraders.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.DistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding

import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import java.util.*
import kotlin.collections.ArrayList


class DistributorAdapter :
    RecyclerView.Adapter<DistributorAdapter.MasterDistributorViewHolder>(
    ), Filterable {


//    interface RvItemClickListener {
//        fun onChildItemClick(parentPosition: Int, childPosition: Int, item: ContestAll?)
//    }
//
//    private var rvItemClickListener: RvItemClickListener? = null
//
//    fun setRvClickListener(rvItemClickListener: RvItemClickListener?) {
//        this.rvItemClickListener = rvItemClickListener
//    }



    private var onItemClickListener: ((DataEmployeeAll) -> Unit)? = null

    fun setOnItemClickListener(listener: (DataEmployeeAll) -> Unit) {
        onItemClickListener = listener
    }


    private var onMenuItemClickListener: ((DataEmployeeAll,DistributionItemListLayoutBinding) -> Unit)? = null

    fun setOnMenuItemClickListener(listener: (DataEmployeeAll,DistributionItemListLayoutBinding) -> Unit) {
        onMenuItemClickListener = listener
    }




    private val differCallback = object :
        DiffUtil.ItemCallback<DataEmployeeAll>() {
        override fun areItemsTheSame(
            oldItem: DataEmployeeAll,
            newItem: DataEmployeeAll
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataEmployeeAll,
            newItem: DataEmployeeAll
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class MasterDistributorViewHolder(var binding: com.paybacktraders.paybacktraders.databinding.DistributionItemListLayoutBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataEmployeeAll, parentPosition: Int) {
            binding.apply {
                tvFullName.text = currentAnnouncement.FullName
                tvPhone.text = currentAnnouncement.Mobile
                tvEmail.text = currentAnnouncement.Email
                tvAddress.text = currentAnnouncement.DeliveryAddress
                tvId.text = currentAnnouncement.id.toString()
                tvRole.text = currentAnnouncement.Role.toString()
                tvPassword.text = currentAnnouncement.Password.toString()
              //  tvStatus.text = currentAnnouncement.Status

                if (currentAnnouncement.Status.equals("1")){
                    tvStatus.text="Active"
                    tvStatus.setTextColor(itemView.context.resources.getColor(R.color.green_aap))
                }else{
                    tvStatus.text="Not Active"

                    tvStatus.setTextColor(itemView.context.resources.getColor(R.color.red))
                }
                itemView.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }

                ivMenuPopUp.setOnClickListener {
                 // showPopupMenu(binding.ivMenuPopUp)
                    onMenuItemClickListener?.let { click->
                        click(currentAnnouncement,binding)
                    }
                }



            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var masterDistributor: MutableList<DataEmployeeAll>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    //function to search in local
    fun filterData(filteredList: List<DataEmployeeAll>) {
        differ.submitList(filteredList)
    }


    private var filteredList: List<DataEmployeeAll> = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterDistributorViewHolder {
        return MasterDistributorViewHolder(
            DistributionItemListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MasterDistributorViewHolder, position: Int) {
        val contest = masterDistributor[position]
        holder.bind(contest, position)


    }

    override fun getItemCount(): Int {
        return masterDistributor.size
    }


//    fun filter(query: String) {
//       // val originalList = differ.currentList.toList()
//        val filteredList = if (query.isEmpty()) {
//            masterDistributor
//        } else {
//            masterDistributor.filter { item ->
//                item.Role.equals(query, ignoreCase = true)
//            }
//        }
//        Log.e("filter", "filter:${masterDistributor.toString()} ", )
//        Log.e("AFTERFILTERfilter", "filter:${filteredList.toString()} ", )
//        differ.submitList(filteredList)
//        notifyDataSetChanged()
//    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString().toLowerCase(Locale.ENGLISH)
                filteredList = if (charString.isEmpty()) {
                    masterDistributor
                } else {
                    val filtered = ArrayList<DataEmployeeAll>()
                    for (item in masterDistributor) {
                        if (item.Role.toLowerCase(Locale.ENGLISH).contains(charString)) {
                            filtered.add(item)
                        }
                    }
                    filtered
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredList = results.values as List<DataEmployeeAll>
                notifyDataSetChanged()
            }
        }
    }


     fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.distributor_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editDist -> {
                    Log.e("EDIT", "showPopupMenu: ", )
                    // Handle option 1 click
                    true
                }
                R.id.menuViewDistributors -> {
                    Log.e("OPTION 2", "showPopupMenu: ", )
                    // Handle option 2 click
                    true
                }
                R.id.menuViewClients -> {
                    // Handle option 3 click
                    Log.e("OPTION 3", "showPopupMenu: ", )
                    true
                }
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }





}