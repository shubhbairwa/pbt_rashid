package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseWithdrawlRequest(
    val `data`: List<DataWithdrawlRequest>,
    val message: String,
    val status: Int
)