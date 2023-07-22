package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseClientStatusRemark(
    val `data`: List<DataClientStatusRemark>,
    val message: String,
    val status: Int
)