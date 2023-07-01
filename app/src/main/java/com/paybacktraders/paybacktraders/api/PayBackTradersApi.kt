package com.paybacktraders.paybacktraders.api

import com.paybacktraders.paybacktraders.models.JsonResponse
import retrofit2.http.GET

interface PayBackTradersApi {

    companion object{
        const val BASE_URL="https://jsonplaceholder.typicode.com/"


    }

    @GET("posts/1/comments")
    suspend fun getComments():JsonResponse


}