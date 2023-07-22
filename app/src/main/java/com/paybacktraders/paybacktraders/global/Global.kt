package com.paybacktraders.paybacktraders.global

import android.app.Activity
import android.app.Dialog
import com.paybacktraders.paybacktraders.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Global {
    const val INTENT_WHERE = "where"
    lateinit var dialog: Dialog

    fun showDialog(context: Activity) {
        dialog = Dialog(context)
        val view = context.layoutInflater.inflate(R.layout.dialog_fullscreen, null, false)
        dialog.getWindow()?.setBackgroundDrawableResource(R.color.transparent);

        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

    }

    fun hideDialog() {
        dialog.dismiss()
    }


    fun formatDateFromMilliseconds(milliseconds: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(milliseconds)
        return dateFormat.format(date)
    }

   // const val BASE_URL = "http://45.132.241.50:8001"

/********TESTMOODEURL**********/
    const val BASE_URL = "https://paybackbytrades.in:8002/"

    /********LIVEMODEURL**********/

    //  const val BASE_URL = "https://paybackbytrades.in:8000/"

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

    const val ID = "_id"
    const val EmployeeCode = "_EmployeeCode"
    const val UserName = "_UserName"
    const val Password = "_Password"
    const val FullName = "_FullName"
    const val Email = "_Email"
    const val Mobile = "_Mobile"
    const val Role = "_Role"
    const val Status = "_Status"
    const val CreatedBy = "_CreatedBy"
    const val ReportingTo = "_ReportingTo"
    const val DeliveryAddress = "_DeliveryAddress"
    const val WalletAmount = "_WalletAmount"
    const val Datetime = "_Datetime"
    const val PAYLOAD_EMPLOYEE_ID="EmployeeId"
    const val PAYLOAD_ID="id"
    const val PAYLOAD_TYPE="Type"
    const val PAYLOAD_FROM_DATE="FromDate"
    const val PAYLOAD_TO_DATE="ToDate"

    const val MASTER_DIST_STRING="Master Distributor"
    const val DISTRIBUTOR_STRING="Distributor"
    const val ADMIN_STRING="Admin"


    fun convertToCustomFormat(dateStr: String?): String {
        val utc = TimeZone.getTimeZone("UTC")
         LocalDateTime.now()
        val sourceFormat = SimpleDateFormat("yyyy-mm-dd")
        val destFormat = SimpleDateFormat("dd-MM-yyyy HH:mm aa")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate)
    }

    fun getDateBeforeTinASTring(string: String): String {
        return string.substringBeforeLast("T")

    }


}