package com.melfouly.paypaldemo.model

import com.google.gson.annotations.SerializedName

data class PaymentSource(
    val cardResult: CardResult
)

data class CardResult(
    val type: String,
    val brand: String,
    @SerializedName("authentication_result") val authenticationResult: AuthenticationResult
)

data class AuthenticationResult(
    @SerializedName("liability_shift") val liabilityShift: String,
    @SerializedName("three_d_secure") val threeDSecure: ThreeDSecure
)

data class ThreeDSecure(
    @SerializedName("enrollment_status") val enrollmentStatus: String,
    @SerializedName("authentication_status") val authenticationStatus: String
)