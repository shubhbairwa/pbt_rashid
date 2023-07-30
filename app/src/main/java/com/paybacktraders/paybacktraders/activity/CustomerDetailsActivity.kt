package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivityCustomerDetailsBinding
import com.paybacktraders.paybacktraders.global.Global
import com.paybacktraders.paybacktraders.model.model.apiresponse.DataCLient
import es.dmoral.toasty.Toasty

class CustomerDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityCustomerDetailsBinding
    var dataclient:DataCLient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataclient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data",DataCLient::class.java)
        }else{
            intent.getParcelableExtra("data")
        }

       // Toasty.success(this,dataclient!!.BrokerName).show()

        binding.toolbarDistributor.apply {
            setOnClickListener {
                finish()
            }
        }

        binding.apply {
            tvCustomerName.text = dataclient!!.FullName
            tvCustomermobile.text = dataclient!!.Mobile
            tvProductName.text=dataclient!!.BrokerName
            tvCustomerStatus.text = dataclient!!.ConnectionStatus
            if (dataclient!!.PaymentStatus.equals("1")){
                tvPaymentSTatus.text="Received"
                tvPaymentSTatus.setTextColor(resources.getColor(R.color.green_aap))
            }else{
                tvPaymentSTatus.text="Not Received"

                tvPaymentSTatus.setTextColor(resources.getColor(R.color.red))
            }

            binding.chipPaymentScreenshot.setOnClickListener{
                if (dataclient!!.PaymentProof.isEmpty()){
                    Toasty.info(this@CustomerDetailsActivity,"NO Data Found").show()
                }else{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Global.PDF_BASE_URL+dataclient!!.PaymentProof))

                    // Check if there's any app that can handle the Intent (e.g., Chrome)

                    // Open the link in the Chrome browser
                    startActivity(intent)
                }

            }
            chipRemarks.setOnClickListener {
                val bundle=Bundle().apply {
                    putString("id",it.id.toString())
                }
                findNavController(R.id.mobile_navigation).navigate(R.id.dialogRemarkStatusBinding,bundle)
            }


            if (dataclient!!.ConnectionStatus.equals("Pending", ignoreCase = true)) {
                tvCustomerStatus.background =
                    resources.getDrawable(R.drawable.background_status_client)
            } else {
                tvCustomerStatus.background =
                resources.getDrawable(R.drawable.background_approved_client)

            }


            tvCustomerRequestDate.text =
                Global.getDateBeforeTinASTring(dataclient!!.Datetime)
            tvTotalEquity.text="$ ${dataclient!!.TotalEquity}"
            tvTradingPassword.text=dataclient!!.TradingAcPass
            tvtradingaccNo.text=dataclient!!.TradingAcNo
            tvEmail.text=dataclient!!.Email

        }


    }


}