package com.paybacktraders.paybacktraders.repository


import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.model.model.apirequestbody.*
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
import java.util.HashMap

interface MainRepos {


    suspend fun doLogin(data: HashMap<String, Any>): Resource<ResponseLogin>
    suspend fun getDashboardData(data: HashMap<String, Any>): Resource<DashBoardResponse>

    suspend fun getDistributor(data: HashMap<String, Any>): Resource<ResponseEmployeeAll>
    suspend fun addDistributor(data: BodyAddDistributor): Resource<ResponseGlobal>
    suspend fun updateDistributor(data: BodyUpdateDistributor): Resource<ResponseGlobal>
    suspend fun updateCustomerStatus(data: BodyClientStatus): Resource<ResponseLogin>
    suspend fun addProduct(data: BodyForAddProduct): Resource<ResponseGlobal>
    suspend fun updateProduct(data: BodyForUpdateProduct): Resource<ResponseGlobal>
    suspend fun getCustomerStatus(data: HashMap<String,Any>): Resource<ResponseClientStatusRemark>
    suspend fun getClientALlFilter(data: HashMap<String, Any>): Resource<ResponseClient>
    suspend fun getProductALlFilter(data: HashMap<String, Any>): Resource<ProductResponse>
    suspend fun getClientAll(): Resource<ResponseClient>
    suspend fun getEmployeeAll(): Resource<ResponseEmployeeAll>
    suspend fun getcontactusall(): Resource<ResponseContactUs>

    suspend fun getBrokerAll(): Resource<ResponseBrokerAll>
    suspend fun getProductAll(): Resource<ProductResponse>




}