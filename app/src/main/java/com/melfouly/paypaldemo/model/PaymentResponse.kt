package com.melfouly.paypaldemo.model

data class PaymentResponse(
    val details: List<Detail>,
    val message: String,
    val name: String
)