package com.cinntra.shivtaradeliveryman.retrofitApi

import com.paybacktraders.paybacktraders.global.Global
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UploadNetworkClient {
    private var retrofit: Retrofit? = null
    private val BASE_URL = Global.BASE_URL;
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}