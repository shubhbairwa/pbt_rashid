package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseBrokerAll(
    val `data`: List<DataBrokerAll>,
    val message: String,
    val status: Int
)