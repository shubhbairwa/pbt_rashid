package com.paybacktraders.paybacktraders.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ClientItemListLayoutBinding
import com.paybacktraders.paybacktraders.databinding.DialogRemarkStatusBinding
import com.paybacktraders.paybacktraders.databinding.ItemClientStatusRemarkBinding
import com.paybacktraders.paybacktraders.databinding.ItemWalletHistoryBinding
import com.paybacktraders.paybacktraders.databinding.ItemWithdrwalRequestBinding
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.*


class WalletWithdrawlAdapter :
    RecyclerView.Adapter<WalletWithdrawlAdapter.MasterDistributorViewHolder>(
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


    private var onItemClickListener: ((DataWithdrawlRequest) -> Unit)? = null

    fun setOnItemClickListener(listener: (DataWithdrawlRequest) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallback = object :
        DiffUtil.ItemCallback<DataWithdrawlRequest>() {
        override fun areItemsTheSame(
            oldItem: DataWithdrawlRequest,
            newItem: DataWithdrawlRequest
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataWithdrawlRequest,
            newItem: DataWithdrawlRequest
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class MasterDistributorViewHolder(var binding: ItemWithdrwalRequestBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataWithdrawlRequest, parentPosition: Int) {
            binding.apply {
                tvrequestId.text = currentAnnouncement.id.toString()
                tvDate.text = Global.getDateBeforeTinASTring(currentAnnouncement.Datetime)
                tvName.text = currentAnnouncement.EmployeeName
                tvStatusPayment.text=currentAnnouncement.Status
                tvAdminRemarks.text="Admin Remark: ${currentAnnouncement.Remarks2}"
                tvRequestRemarks.text="Req Remark: ${currentAnnouncement.Remarks}"
                if (currentAnnouncement.Status.equals("Approved",ignoreCase = true)){
                    tvStatusPayment.background =
                        itemView.context.resources.getDrawable(R.drawable.background_approved_client)
                }else{
                    tvStatusPayment.background =
                        itemView.context.resources.getDrawable(R.drawable.background_status_client)
                }
             //   tvDistributorName.text = currentAnnouncement.Status
                tvAmount.text=currentAnnouncement.Amount
                tvUsdt.text=currentAnnouncement.USDT
                tvStatusPayment.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }


            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var masterDistributor: MutableList<DataWithdrawlRequest>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    //function to search in local
    fun filterData(filteredList: List<DataWithdrawlRequest>) {
        differ.submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterDistributorViewHolder {
        return MasterDistributorViewHolder(
            ItemWithdrwalRequestBinding.inflate(
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