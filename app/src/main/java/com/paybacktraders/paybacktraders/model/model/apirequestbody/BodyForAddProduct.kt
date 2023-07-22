package com.paybacktraders.paybacktraders.model.model.apirequestbody

data class BodyForAddProduct(
    val BrokerName: String,
    val MaxDD: String,
    val ProductCode: String,
    val ProductName: String,
    val ProfitMA: String
)