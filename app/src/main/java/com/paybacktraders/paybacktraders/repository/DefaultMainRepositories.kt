package com.paybacktraders.paybacktraders.repository

import com.paybacktraders.paybacktraders.api.ApiClient
import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.apihelper.safeCall
import com.paybacktraders.paybacktraders.model.model.apiresponse.ResponseLogin
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



}