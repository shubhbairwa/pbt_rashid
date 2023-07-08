package com.paybacktraders.paybacktraders.model.model.apiresponse

data class DataCLient(
    val BrokerName: String,
    val ConnectionStatus: String,
    val CreatedBy: String,
    val CustomerCode: String,
    val Datetime: String,
    val Email: String,
    val FullName: String,
    val Mobile: String,
    val PaymentProof: String,
    val PaymentStatus: String,
    val ProductId: String,
    val Status: String,
    val TotalEquity: String,
    val TradingAcNo: String,
    val TradingAcPass: String,
    val id: Int
)