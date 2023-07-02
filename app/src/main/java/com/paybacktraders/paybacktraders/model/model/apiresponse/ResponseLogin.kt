package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseLogin(
    val `data`: List<DataLogin>,
    val message: String,
    val status: Int
)