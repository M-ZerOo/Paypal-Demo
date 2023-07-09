package com.melfouly.paypaldemo.network

import com.melfouly.paypaldemo.model.OrderRequest
import com.melfouly.paypaldemo.model.OrderResponse
import com.melfouly.paypaldemo.model.PaymentResponse
import com.paypal.android.cardpayments.Card
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {
        // Creates order and returns order id
        @POST("v2/checkout/orders")
        fun createOrder(
                @Header("Authorization") accessKey: String,
                @Body orderRequest: OrderRequest
        ): Single<OrderResponse>

        @POST("v2/checkout/orders/{orderId}/authorize")
        fun confirmPayment(
                @Header("PayPal-Request-Id") requestId: String,
                @Header("Authorization") accessKey: String,
                @Path("orderId") orderId: String,
                @Body card: Card
        ): Single<PaymentResponse>

}