package com.paybacktraders.paybacktraders.model.model.apiresponse

data class ProductResponse(
    val `data`: MutableList<DataProduct>,
    val message: String,
    val status: Int
)