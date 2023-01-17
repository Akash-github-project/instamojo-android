package com.recharge.instamojo_poc.api

data class OrderIdModel(
    val amount: String,
    val buyer_name: String,
    val email: String,
    val phone: String,
    val purpose: String
)