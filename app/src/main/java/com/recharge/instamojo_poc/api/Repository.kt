package com.recharge.instamojo_poc.api

import retrofit2.Response

object Repository {
    private val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
    suspend fun getOrderIdFormServer(body:OrderIdModel): Response<OrderCreatedResponse> {
        return apiService.getOrderId(body)
    }
}