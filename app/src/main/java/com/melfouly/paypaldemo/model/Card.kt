package com.melfouly.paypaldemo.model

data class Card(
    val number: String,
    val expiry: String,
    val name: String,
    val billingAddress: BillingAddress
)

data class BillingAddress(
    val addressLine1: String,
    val addressLine2: String,
    val adminArea1: String,
    val adminArea2: String,
    val postalCode: String,
    val countryCode: String
)
