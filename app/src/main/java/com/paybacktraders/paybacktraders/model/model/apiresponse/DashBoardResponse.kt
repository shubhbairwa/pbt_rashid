package com.paybacktraders.paybacktraders.model.model.apiresponse

data class DashBoardResponse(
    val `data`: List<DataDashBoard>,
    val message: String,
    val status: Int
)