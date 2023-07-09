package com.melfouly.paypaldemo.model

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token") val token: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)
