package com.melfouly.paypaldemo.repository

import com.melfouly.paypaldemo.model.AccessToken
import com.melfouly.paypaldemo.model.OrderRequest
import com.melfouly.paypaldemo.model.OrderResponse
import com.melfouly.paypaldemo.model.PaymentResponse
import com.melfouly.paypaldemo.network.OrderApiService
import com.melfouly.paypaldemo.network.TokenApiService
import com.paypal.android.cardpayments.Card
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class RepositoryImpl(
    private val orderApiService: OrderApiService,
    private val tokenApiService: TokenApiService
) :
    Repository {
    override fun requestAccessToken(): Observable<AccessToken> = tokenApiService.requestToken()

    override fun createOrder(accessKey: String, orderRequest: OrderRequest): Single<OrderResponse> =
        orderApiService.createOrder(accessKey, orderRequest)

    override fun confirmPayment(
        requestId: String,
        accessKey: String,
        orderId: String,
        card: Card
    ): Single<PaymentResponse> = orderApiService.confirmPayment(requestId, accessKey, orderId, card)
}