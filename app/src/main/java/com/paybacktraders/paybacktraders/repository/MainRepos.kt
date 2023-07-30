package com.paybacktraders.paybacktraders.repository


import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.model.model.apirequestbody.*
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
import java.util.HashMap

interface MainRepos {


    suspend fun doLogin(data: HashMap<String, Any>): Resource<ResponseLogin>
    suspend fun getDashboardData(data: HashMap<String, Any>): Resource<DashBoardResponse>
    suspend fun getWalletHistory(data: HashMap<String, Any>): Resource<ResponseWalletHistory>
    suspend fun withdrawlApproval(data: HashMap<String, Any>): Resource<ResponseGlobal>
    suspend fun sendWithDrawlrequest(data: HashMap<String, Any>): Resource<ResponseGlobal>
    suspend fun getWithdrawlRequestList(data: HashMap<String, Any>): Resource<ResponseWithdrawlRequest>
    suspend fun getDistributor(data: HashMap<String, Any>): Resource<ResponseEmployeeAll>
    suspend fun addDistributor(data: BodyAddDistributor): Resource<ResponseGlobal>
    suspend fun updateDistributor(data: BodyUpdateDistributor): Resource<ResponseGlobal>
    suspend fun updateCustomerStatus(data: BodyClientStatus): Resource<ResponseLogin>
    suspend fun addProduct(data: BodyForAddProduct): Resource<ResponseGlobal>
    suspend fun updateProduct(data: BodyForUpdateProduct): Resource<ResponseGlobal>
    suspend fun getCustomerStatus(data: HashMap<String,Any>): Resource<ResponseClientStatusRemark>
    suspend fun getClientALlFilter(data: HashMap<String, Any>): Resource<ResponseClient>
    suspend fun getProductALlFilter(data: HashMap<String, Any>): Resource<ProductResponse>
   // suspend fun getProductALlFilter(): Resource<ProductResponse>
    suspend fun getClientAll(): Resource<ResponseClient>
    suspend fun getEmployeeAll(): Resource<ResponseEmployeeAll>
    suspend fun getcontactusall(): Resource<ResponseContactUs>
    suspend fun getBrokerAll(): Resource<ResponseBrokerAll>
    suspend fun getProductAll(): Resource<ProductResponse>
    suspend fun getForgotPasswordEmail(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getOtpVerify(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getPasswordChange(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getProfileDetailOneApi(data:JsonObject): Resource<ResponseEmployeeAll>




}