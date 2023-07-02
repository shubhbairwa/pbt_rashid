package com.paybacktraders.paybacktraders.repository


import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.model.model.apiresponse.ResponseLogin
import java.util.HashMap

interface MainRepos {


    suspend fun doLogin(data: HashMap<String, Any>): Resource<ResponseLogin>




}