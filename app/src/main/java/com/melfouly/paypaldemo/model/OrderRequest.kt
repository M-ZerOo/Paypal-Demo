package com.melfouly.paypaldemo.model

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    val intent: String,
    @SerializedName("purchase_units") val purchaseUnits: List<PurchaseUnit>
)

