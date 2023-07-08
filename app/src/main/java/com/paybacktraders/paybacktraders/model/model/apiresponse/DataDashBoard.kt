package com.paybacktraders.paybacktraders.model.model.apiresponse

data class DataDashBoard(
    val totalAmountInWallet: String,
    val totalConnectedCustomer: Int,
    val totalCustomer: Int,
    val totalDisconnectedCustomer: Int,
    val totalDistributor: Int,
    val totalPendingCustomer: Int,
    val totalProduct: Int
)