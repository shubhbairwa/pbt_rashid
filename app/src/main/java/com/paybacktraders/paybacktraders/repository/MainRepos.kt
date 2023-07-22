package com.paybacktraders.paybacktraders.repository


import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyAddDistributor
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyUpdateDistributor
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
import java.util.HashMap

interface MainRepos {


    suspend fun doLogin(data: HashMap<String, Any>): Resource<ResponseLogin>
    suspend fun getDashboardData(data: HashMap<String, Any>): Resource<DashBoardResponse>

    suspend fun getDistributor(data: HashMap<String, Any>): Resource<ResponseEmployeeAll>
    suspend fun addDistributor(data: BodyAddDistributor): Resource<ResponseGlobal>
    suspend fun updateDistributor(data: BodyUpdateDistributor): Resource<ResponseGlobal>
    suspend fun updateCustomerStatus(data: BodyClientStatus): Resource<ResponseLogin>
    suspend fun getClientALlFilter(data: HashMap<String, Any>): Resource<ResponseClient>
    suspend fun getProductALlFilter(data: HashMap<String, Any>): Resource<ProductResponse>
    suspend fun getClientAll(): Resource<ResponseClient>
    suspend fun getEmployeeAll(): Resource<ResponseEmployeeAll>
    suspend fun getProductAll(): Resource<ProductResponse>
    suspend fun getForgotPasswordEmail(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getOtpVerify(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getPasswordChange(data:JsonObject): Resource<ResponseGlobal>
    suspend fun getProfileDetailOneApi(data:JsonObject): Resource<ResponseEmployeeAll>




}