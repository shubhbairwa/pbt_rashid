package com.paybacktraders.paybacktraders.activity


import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivityLoginBinding
import com.paybacktraders.paybacktraders.global.Global
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var selectedItem = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Global.showDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
           delay(2000)
            Global.hideDialog()
        }
//        val dialog=Dialog(this)
//        val view=this.layoutInflater.inflate(R.layout.dialog_fullscreen,null,false)
//        dialog.setContentView(view)
//        dialog.setCancelable(false)
//        dialog.show()


        // Create an array of items for the dropdown
        val items = resources.getStringArray(R.array.user_type)

        // Create an ArrayAdapter using the item array and a default dropdown layout
        val adapter = ArrayAdapter(
            this,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            items
        )

        // Set the adapter to the dropdown
        binding.dropdown.setAdapter(adapter)

        // Set item selector listener
        binding.dropdown.setOnItemClickListener { parent, view, position, id ->
            selectedItem = parent.getItemAtPosition(position).toString()
            // Do something with the selected item
            // For example, display a toast message
            Toast.makeText(this, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
        }


        binding.loginButton.setOnClickListener {
            if (selectedItem.equals("Admin", ignoreCase = true)) {
                Intent(this, AdminActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(this, MasterDistributorActivity::class.java).also {
                    startActivity(it)
                }
            }

        }


    }


}