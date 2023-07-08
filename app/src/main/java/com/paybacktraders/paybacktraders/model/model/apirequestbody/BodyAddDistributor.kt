package com.paybacktraders.paybacktraders.model.model.apirequestbody

data class BodyAddDistributor(
    val CreatedBy: Int,
    val DeliveryAddress: String,
    val Email: String,
    val FullName: String,
    val Mobile: String,
    val Password: String,
    val ReportingTo: Int,
    val Role: String,
    val UserName: String
)