package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivityDistributorDetailsBinding
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataEmployeeAll

class DistributorDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityDistributorDetailsBinding
    var dataEmployeeAll:DataEmployeeAll?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDistributorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataEmployeeAll = intent.getSerializableExtra("dist") as DataEmployeeAll


        binding.apply {
            tvDetails.text=dataEmployeeAll!!.FullName
            ivBack.setOnClickListener {
                finish()
            }
            tvFullName.text = dataEmployeeAll!!.FullName
            tvPhone.text = dataEmployeeAll!!.Mobile
            tvEmail.text = dataEmployeeAll!!.Email
            tvAddress.text = dataEmployeeAll!!.DeliveryAddress
            tvId.text = dataEmployeeAll!!.id.toString()
            tvRole.text = dataEmployeeAll!!.Role.toString()
            tvPassword.text = dataEmployeeAll!!.Password.toString()
            //  tvStatus.text = dataEmployeeAll!!.Status

            if (dataEmployeeAll!!.Status.equals("1")){
                tvStatus.text="Active"
                tvStatus.setTextColor(resources.getColor(R.color.green_aap))
            }else{
                tvStatus.text="Not Active"

                tvStatus.setTextColor(resources.getColor(R.color.red))
            }




        }

    }
}