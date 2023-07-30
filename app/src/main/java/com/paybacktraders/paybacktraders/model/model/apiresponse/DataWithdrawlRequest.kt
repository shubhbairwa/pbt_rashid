package com.paybacktraders.paybacktraders.model.model.apiresponse

data class DataWithdrawlRequest(
    val Amount: String,
    val CreatedBy: String,
    val Datetime: String,
    val EmployeeId: String,
    val EmployeeName: String,
    val Remarks: String,
    val Remarks2: String,
    val Status: String,
    val USDT: String,
    val id: Int
)