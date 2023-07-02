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

    const val BASE_URL="http://45.132.241.50:8000/"

/**
            "id": 1,
            "EmployeeCode": "Emp1",
            "UserName": "1234567890",
            "Password": "admin@123",
            "FullName": "admin",
            "Email": "admin@gmail.com",
            "Mobile": "1234567890",
            "Role": "Admin",
            "Status": "1",
            "CreatedBy": "0",
            "ReportingTo": "0",
            "DeliveryAddress": "Abc admin",
            "WalletAmount": "0",
            "Datetime": "2023-06-11T14:55:43.449763+05:30"

    **/

    const val ID="_id"
    const val EmployeeCode="_EmployeeCode"
    const val UserName="_UserName"
    const val Password="_Password"
    const val FullName="_FullName"
    const val Email="_Email"
    const val Mobile="_Mobile"
    const val Role="_Role"
    const val Status="_Status"
    const val CreatedBy="_CreatedBy"
    const val ReportingTo="_ReportingTo"
    const val DeliveryAddress="_DeliveryAddress"
    const val WalletAmount="_WalletAmount"
    const val Datetime="_Datetime"

}