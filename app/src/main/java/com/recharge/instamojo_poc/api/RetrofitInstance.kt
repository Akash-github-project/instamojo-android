package com.recharge.instamojo_poc.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://wild-pink-python-cuff.cyclic.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}