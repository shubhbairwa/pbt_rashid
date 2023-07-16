package com.paybacktraders.paybacktraders.api


import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyAddDistributor
import com.paybacktraders.paybacktraders.model.model.apirequestbody.BodyClientStatus
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

    @GET("customer/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getClientAll(): Response<ResponseClient>

    @POST("customer/all_filter")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getClientFilter(@Body data : HashMap<String,Any>): Response<ResponseClient>

    @POST("product/all_filter")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getProductFilter(@Body data : HashMap<String,Any>): Response<ProductResponse>

    @GET("employee/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getEmployeeALl(): Response<ResponseEmployeeAll>


    @GET("product/all")
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun getProductALl(): Response<ProductResponse>

    @POST("customer/create")
    fun customerCreate(@Body request : MultipartBody): Call<ResponseLogin>

    @POST("customer/approval")
   suspend fun customerApproval(@Body request : BodyClientStatus): Response<ResponseLogin>









}
