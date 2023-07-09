package com.melfouly.paypaldemo.repository

import com.melfouly.paypaldemo.model.AccessToken
import com.melfouly.paypaldemo.model.OrderRequest
import com.melfouly.paypaldemo.model.OrderResponse
import com.melfouly.paypaldemo.model.PaymentResponse
import com.paypal.android.cardpayments.Card
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface Repository {
    fun requestAccessToken(): Observable<AccessToken>
    fun createOrder(accessKey: String, orderRequest: OrderRequest): Single<OrderResponse>
    fun confirmPayment(requestId: String, accessKey: String, orderId: String, card: Card): Single<PaymentResponse>
}