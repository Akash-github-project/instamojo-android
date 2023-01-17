package com.recharge.instamojo_poc.api
data class OrderCreatedResponse(
    val created: Created
)

data class Created(
    val amount: String,
    val email: String,
    val name: String,
    val order_id: String,
    val phone: String
)