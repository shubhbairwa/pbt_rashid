package com.paybacktraders.paybacktraders.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.databinding.ItemContactUsAllBinding
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataContactUs


class ContactUsAdapter :
    RecyclerView.Adapter<ContactUsAdapter.ProductViewHolder>(
    ) {

    private var onItemCallClickListener: ((DataContactUs) -> Unit)? = null
    fun setOnItemClickListener(listener: (DataContactUs) -> Unit) {
        onItemCallClickListener = listener
    }

    private var onFullItemClickListener: ((DataContactUs) -> Unit)? = null
    fun setOnFullItemClickListener(listener: (DataContactUs) -> Unit) {
        onFullItemClickListener = listener
    }


    private var onItemMailClickListener: ((DataContactUs) -> Unit)? = null
    fun setOnMailItemClickListener(listener: (DataContactUs) -> Unit) {
        onItemMailClickListener = listener
    }
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
        DiffUtil.ItemCallback<DataContactUs>() {
        override fun areItemsTheSame(
            oldItem: DataContactUs,
            newItem: DataContactUs
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataContactUs,
            newItem: DataContactUs
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class ProductViewHolder(var binding: ItemContactUsAllBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataContactUs, parentPosition: Int) {
            binding.apply {
                tvNameOfParent.text = currentAnnouncement.Name
                chiplocation.text=currentAnnouncement.Country
                tvMsg.text=currentAnnouncement.Msg

                chipCall.setOnClickListener {
                    onItemCallClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }

                chipMail.setOnClickListener {
                    onItemMailClickListener?.let { click ->
                        click(currentAnnouncement)

                    }
                }

                itemView.setOnClickListener {
                    onFullItemClickListener?.let { click ->
                        click(currentAnnouncement)

                    }
                }

            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var product: MutableList<DataContactUs>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemContactUsAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val contest = product[position]
        holder.bind(contest, position)


    }

    override fun getItemCount(): Int {
        return product.size
    }


}