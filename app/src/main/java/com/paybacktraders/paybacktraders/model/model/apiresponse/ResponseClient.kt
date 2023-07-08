package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseClient(
    val `data`: MutableList<DataCLient>,
    val message: String,
    val status: Int
)