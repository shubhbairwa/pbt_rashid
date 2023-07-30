package com.paybacktraders.paybacktraders.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.databinding.ClientItemListLayoutBinding
import com.paybacktraders.paybacktraders.databinding.DialogRemarkStatusBinding
import com.paybacktraders.paybacktraders.databinding.ItemClientStatusRemarkBinding
import com.paybacktraders.paybacktraders.databinding.ItemWalletHistoryBinding
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.*


class WalletHistoryAdapter :
    RecyclerView.Adapter<WalletHistoryAdapter.MasterDistributorViewHolder>(
    ) {


//    interface RvItemClickListener {
//        fun onChildItemClick(parentPosition: Int, childPosition: Int, item: ContestAll?)
//    }
//
//    private var rvItemClickListener: RvItemClickListener? = null
//
//    fun setRvClickListener(rvItemClickListener: RvItemClickListener?) {
//        this.rvItemClickListener = rvItemClickListener
//    }


    private var onItemClickListener: ((DataWalletHistory) -> Unit)? = null

    fun setOnItemClickListener(listener: (DataWalletHistory) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallback = object :
        DiffUtil.ItemCallback<DataWalletHistory>() {
        override fun areItemsTheSame(
            oldItem: DataWalletHistory,
            newItem: DataWalletHistory
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataWalletHistory,
            newItem: DataWalletHistory
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class MasterDistributorViewHolder(var binding: ItemWalletHistoryBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataWalletHistory, parentPosition: Int) {
            binding.apply {
                tvHistoryId.text = currentAnnouncement.id.toString()
                tvDate.text = Global.getDateBeforeTinASTring(currentAnnouncement.Datetime)
                tvName.text = currentAnnouncement.CustomerName
                tvDistributorName.text = "Dist. Name:-  ${currentAnnouncement.EmployeeName}"
                tvAmount.text=currentAnnouncement.Amount
                tvRemark.text=currentAnnouncement.Remarks
                itemView.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }


            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var masterDistributor: MutableList<DataWalletHistory>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    //function to search in local
    fun filterData(filteredList: List<DataWalletHistory>) {
        differ.submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterDistributorViewHolder {
        return MasterDistributorViewHolder(
            ItemWalletHistoryBinding.inflate(
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


}