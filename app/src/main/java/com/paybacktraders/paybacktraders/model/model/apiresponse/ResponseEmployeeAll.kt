package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ResponseEmployeeAll(
    val `data`: MutableList<DataEmployeeAll>,
    val message: String,
    val status: Int
)