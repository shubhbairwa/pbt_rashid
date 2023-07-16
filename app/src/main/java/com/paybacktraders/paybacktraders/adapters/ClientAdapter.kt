package com.paybacktraders.paybacktraders.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ClientItemListLayoutBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataProduct


class ClientAdapter :
    RecyclerView.Adapter<ClientAdapter.AnnouncementViewHolder>(
    ) {

    private var onItemClickListener: ((DataCLient) -> Unit)? = null
    fun setOnItemClickListener(listener: (DataCLient) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallback = object :
        DiffUtil.ItemCallback<DataCLient>() {
        override fun areItemsTheSame(
            oldItem: DataCLient,
            newItem: DataCLient
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataCLient,
            newItem: DataCLient
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class AnnouncementViewHolder(var binding: ClientItemListLayoutBinding) :
        ViewHolder(binding.root) {


        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(currentAnnouncement: DataCLient, parentPosition: Int) {
            binding.apply {
                tvCustomerName.text = currentAnnouncement.FullName
                tvCustomermobile.text = currentAnnouncement.Mobile
                tvCustomerStatus.setOnClickListener {
                    onItemClickListener?.let { click->
                        click(currentAnnouncement)
                    }
                }

                tvCustomerStatus.text = currentAnnouncement.ConnectionStatus

                if (currentAnnouncement.ConnectionStatus.equals("Pending", ignoreCase = true)) {
                    tvCustomerStatus.background =
                        itemView.context.resources.getDrawable(R.drawable.background_status_client)
                } else {
                    tvCustomerStatus.background =
                        itemView.context.resources.getDrawable(R.drawable.background_approved_client)

                }





                tvCustomerRequestDate.text =
                    Global.getDateBeforeTinASTring(currentAnnouncement.Datetime)
            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var announcement: MutableList<DataCLient>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        return AnnouncementViewHolder(
            ClientItemListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val contest = announcement[position]
        holder.bind(contest, position)
        //childMembersAdapter.childItemClickListener = holder


    }

    override fun getItemCount(): Int {
        return announcement.size
    }


}