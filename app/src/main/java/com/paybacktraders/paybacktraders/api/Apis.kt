package com.paybacktraders.paybacktraders.api


import com.paybacktraders.paybacktraders.model.model.apiresponse.ResponseLogin
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

    @POST("customer/create")
    fun customerCreate(@Body request : MultipartBody): Call<ResponseLogin>




}
