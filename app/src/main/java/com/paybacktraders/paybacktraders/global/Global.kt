package com.paybacktraders.paybacktraders.global

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.paybacktraders.paybacktraders.R

object Global {
    lateinit var dialog: Dialog

    fun showDialog(context:Activity){
         dialog= Dialog(context)
        val view=context.layoutInflater.inflate(R.layout.dialog_fullscreen,null,false)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

    }
    fun hideDialog(){
        dialog.dismiss()
    }
}