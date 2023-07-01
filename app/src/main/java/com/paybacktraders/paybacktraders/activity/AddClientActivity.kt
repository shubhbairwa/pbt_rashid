package com.paybacktraders.paybacktraders.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paybacktraders.paybacktraders.databinding.ActivityAddClientBinding

class AddClientActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}