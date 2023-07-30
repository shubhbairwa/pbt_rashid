package com.paybacktraders.paybacktraders.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paybacktraders.paybacktraders.databinding.ItemProductBinding
import com.paybacktraders.paybacktraders.databinding.MasterDistributionItemListLayoutBinding
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataProduct


class ProductAdapter :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(
    ) {

    private var onItemClickListener: ((DataProduct) -> Unit)? = null
    fun setOnItemClickListener(listener: (DataProduct) -> Unit) {
        onItemClickListener = listener
    }

    private var onFullItemClickListener: ((DataProduct) -> Unit)? = null
    fun setOnFullItemClickListener(listener: (DataProduct) -> Unit) {
        onFullItemClickListener = listener
    }


    private var onItemAddClientClickListener: ((DataProduct) -> Unit)? = null
    fun setOnAddClientItemClickListener(listener: (DataProduct) -> Unit) {
        onItemAddClientClickListener = listener
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
        DiffUtil.ItemCallback<DataProduct>() {
        override fun areItemsTheSame(
            oldItem: DataProduct,
            newItem: DataProduct
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: DataProduct,
            newItem: DataProduct
        ): Boolean {
            return oldItem == newItem
        }

    }


    inner class ProductViewHolder(var binding: ItemProductBinding) :
        ViewHolder(binding.root) {


        fun bind(currentAnnouncement: DataProduct, parentPosition: Int) {
            binding.apply {
                tvProductId.text = currentAnnouncement.ProductCode
                tvBrokerName.text = currentAnnouncement.BrokerName
                tvMaxDD.text = currentAnnouncement.MaxDD
                tvBoatName.text = currentAnnouncement.ProductName
                tvProfit.text = currentAnnouncement.ProfitMA.toString()
                tvPrice.text="$ ${currentAnnouncement.ProductFee}"

                tvShareLink.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currentAnnouncement)
                    }
                }

                chipAddProuct.setOnClickListener {
                    onItemAddClientClickListener?.let { click ->
                        click(currentAnnouncement)

                    }
                }

                itemView.setOnClickListener {
                    onFullItemClickListener?.let { click->
                        click(currentAnnouncement)

                    }
                }

            }
        }


    }


    val differ = AsyncListDiffer(this, differCallback)
    var product: MutableList<DataProduct>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    //function to search in local
    fun filterData(filteredList: List<DataProduct>) {
        differ.submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
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