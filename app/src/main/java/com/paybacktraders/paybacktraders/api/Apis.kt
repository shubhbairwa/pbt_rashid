package com.paybacktraders.paybacktraders.api


import com.google.gson.JsonObject
import com.paybacktraders.paybacktraders.model.model.apirequestbody.*
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Apis {


//    @Multipart
//  @POST("tickets/ticketsigninconfirm")
//    fun signandconfirm(
//        @Part data: MultipartBody.Part,
//        @Part("EmployeeId") employeeid: RequestBody,
//        @Part("TicketId") ticketid: RequestBody,
//        @Part("CustomerFeedback") CustomerFeedback: RequestBody
//    ): Call<TicketDetailsModel>







    /*****NEW API RESPONSE WITH RESOURCE CLASSES****/
    @POST("employee/login")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun doLogin(@Body data : HashMap<String,Any>): Response<ResponseLogin>

    @POST("employee/dashboard")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getDashboardData(@Body data : HashMap<String,Any>): Response<DashBoardResponse>

    @POST("employee/all_filter")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getDistributor(@Body data : HashMap<String,Any>): Response<ResponseEmployeeAll>

    @POST("employee/create")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun addDistributor(@Body data : BodyAddDistributor): Response<ResponseGlobal>

    @POST("employee/update")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun updateDistributor(@Body data : BodyUpdateDistributor): Response<ResponseGlobal>

    @GET("customer/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getClientAll(): Response<ResponseClient>

    @POST("customer/all_filter")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getClientFilter(@Body data : HashMap<String,Any>): Response<ResponseClient>

    @POST("product/all_filter")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getProductFilter(@Body data : HashMap<String,Any>): Response<ProductResponse>

    @POST("product/create")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun addProduct(@Body data : BodyForAddProduct): Response<ResponseGlobal>

    @POST("product/update")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun updateProduct(@Body data : BodyForUpdateProduct): Response<ResponseGlobal>

    @GET("employee/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getEmployeeALl(): Response<ResponseEmployeeAll>

    @GET("contactus/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getContactUsAll(): Response<ResponseContactUs>






    @GET("product/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getProductALl(): Response<ProductResponse>

    @POST("customer/create")
    fun customerCreate(@Body request : MultipartBody): Call<ResponseLogin>

    @POST("customer/approval")
   suspend fun customerApproval(@Body request : BodyClientStatus): Response<ResponseLogin>


    @GET("/broker/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getBrokerAll(): Response<ResponseBrokerAll>


    @POST("customer/all_status_remarks")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getCustomerStatus(@Body data : HashMap<String,Any>): Response<ResponseClientStatusRemark>



    @POST("employee/forget_password")
    suspend fun getForgotPasswordEmail(@Body jsonObject: JsonObject): Response<ResponseGlobal>

    @POST("employee/verify_otp")
    suspend fun getOtpVerify(@Body jsonObject: JsonObject): Response<ResponseGlobal>

    @POST("employee/change_password")
    suspend fun getPasswordChange(@Body jsonObject: JsonObject): Response<ResponseGlobal>

    @POST("employee/one")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getProfileDetailOneApi(@Body jsonObject: JsonObject): Response<ResponseEmployeeAll>



}
