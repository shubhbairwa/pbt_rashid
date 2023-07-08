package com.paybacktraders.paybacktraders.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.databinding.ClientItemListLayoutBinding
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll


class MasterDistributorAdapter :
    RecyclerView.Adapter<MasterDistributorAdapter.MasterDistributorViewHolder>(
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


    inner class MasterDistributorViewHolder(var binding: MasterDistributionItemListLayoutBinding) :
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
                tvStatus.text = currentAnnouncement.Status




            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var masterDistributor: MutableList<DataEmployeeAll>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterDistributorViewHolder {
        return MasterDistributorViewHolder(
            MasterDistributionItemListLayoutBinding.inflate(
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


}