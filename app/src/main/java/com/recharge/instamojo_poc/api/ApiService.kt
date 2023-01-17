package com.recharge.instamojo_poc.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/create")
    suspend fun getOrderId(@Body body:OrderIdModel):Response<OrderCreatedResponse>
}