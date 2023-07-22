package com.paybacktraders.paybacktraders.repository

import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.apihelper.safeCall
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyAddDistributor
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyUpdateDistributor
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.HashMap


class DefaultMainRepositories : MainRepos {
//    override suspend fun loginUser(data: java.util.HashMap<String, String>) = withContext(Dispatchers.IO) {
//        safeCall {
//            val response = ApiClient().service.loginEmployeeNew(data)
//            Resource.Success(response.body()!!)
//        }
//    }

    override suspend fun doLogin(data: HashMap<String, Any>)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.doLogin(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getDashboardData(data: HashMap<String, Any>)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getDashboardData(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getDistributor(data: HashMap<String, Any>)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getDistributor(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun addDistributor(data: BodyAddDistributor)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.addDistributor(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun updateDistributor(data: BodyUpdateDistributor)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.updateDistributor(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun updateCustomerStatus(data: BodyClientStatus)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.customerApproval(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getClientALlFilter(data: HashMap<String, Any>)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getClientFilter(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getProductALlFilter(data: HashMap<String, Any>)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getProductFilter(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getClientAll()= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getClientAll()
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getEmployeeAll()= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getEmployeeALl()
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getProductAll()= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getProductALl()
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getForgotPasswordEmail(data: JsonObject)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getForgotPasswordEmail(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getOtpVerify(data: JsonObject)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getOtpVerify(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getPasswordChange(data: JsonObject)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getPasswordChange(data)
            Resource.Success(response.body()!!)
        }
    }

    override suspend fun getProfileDetailOneApi(data: JsonObject)= withContext(Dispatchers.IO){
        safeCall {
            val response = ApiClient().service.getProfileDetailOneApi(data)
            Resource.Success(response.body()!!)
        }
    }
}