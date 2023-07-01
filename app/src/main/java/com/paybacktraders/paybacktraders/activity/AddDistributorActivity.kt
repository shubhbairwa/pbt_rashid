package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paybacktraders.paybacktraders.databinding.ActivityAddDistributorBinding

class AddDistributorActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddDistributorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddDistributorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}