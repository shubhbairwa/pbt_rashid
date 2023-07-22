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
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataClientStatusRemark
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll


class ClientStatusRemarkAdapter :
    RecyclerView.Adapter<ClientStatusRemarkAdapter.MasterDistributorViewHolder>(
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




    private var onItemClickListener: ((DataClientStatusRemark) -> Unit)? = null

    fun setOnItemClickListener(listener: (DataClientStatusRemark) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallback = object :
        DiffUtil.ItemCallback<DataClientStatusRemark>() {
        override fun areItemsTheSame(
            oldItem: DataClientStatusRemark,
            newItem: DataClientStatusRemark
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataClientStatusRemark,
            newItem: DataClientStatusRemark
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class MasterDistributorViewHolder(var binding: ItemClientStatusRemarkBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataClientStatusRemark, parentPosition: Int) {
            binding.apply {
                tvRemark.text=currentAnnouncement.Remarks
                tvDate.text=Global.getDateBeforeTinASTring(currentAnnouncement.Datetime)

                itemView.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }


            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var masterDistributor: MutableList<DataClientStatusRemark>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterDistributorViewHolder {
        return MasterDistributorViewHolder(
            ItemClientStatusRemarkBinding.inflate(
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