package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseWalletHistory(
    val `data`: List<DataWalletHistory>,
    val message: String,
    val status: Int
)