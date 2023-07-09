package com.melfouly.paypaldemo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melfouly.paypaldemo.model.AccessToken
import com.melfouly.paypaldemo.model.PaymentResponse
import com.melfouly.paypaldemo.repository.Repository
import com.paypal.android.cardpayments.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PaypalPaymentViewModel @Inject constructor(private val repositoryImpl: Repository) :
    ViewModel() {

    private val TAG = "ViewModel"
    private val requestId = "7b92603e-77ed-4896-8e78-5dea2050476a"

    private val _accessToken = MutableLiveData<AccessToken>()
    val accessToken: LiveData<AccessToken> get() = _accessToken

    private val _paymentResponse = MutableLiveData<PaymentResponse>()
    val paymentResponse: LiveData<PaymentResponse> get() = _paymentResponse

    private lateinit var accessKey: String

    private lateinit var accessTokenDisposable: Disposable
    private lateinit var paymentResponseDisposable: Disposable

    fun requestAccessToken() {
        val accessTokenObservable = repositoryImpl.requestAccessToken()
        accessTokenObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(setAccessTokenObserver())
    }

    private fun setAccessTokenObserver(): Observer<AccessToken> {
        return object : Observer<AccessToken> {
            override fun onSubscribe(d: Disposable) {
                accessTokenDisposable = d
            }

            override fun onNext(t: AccessToken) {
                _accessToken.value = t
                accessKey = "Bearer ${t.token}"
                Log.d(
                    TAG,
                    "onNext: Expire: ${t.expiresIn}, Token: ${t.token}, TokenType: ${t.tokenType}"
                )
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError: setAccessTokenObserver ${e.message}")
                Log.d(TAG, "onError: setAccessTokenObserver ${e.localizedMessage}")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: setAccessTokenObserver")
            }
        }
    }

    fun confirmPayment(orderId: String, card: Card) {
        val confirmPaymentObservable =
            repositoryImpl.confirmPayment(requestId, accessKey, orderId, card)
        confirmPaymentObservable.subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(setConfirmPaymentObserver())
    }

    private fun setConfirmPaymentObserver(): SingleObserver<PaymentResponse> {
        return object : SingleObserver<PaymentResponse> {
            override fun onSubscribe(d: Disposable) {
                paymentResponseDisposable = d
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "setConfirmPaymentObserver onError: ${e.message}")
            }

            override fun onSuccess(t: PaymentResponse) {
                Log.d(TAG, "setConfirmPaymentObserver onNext: ${t.name}")
                Log.d(TAG, "setConfirmPaymentObserver onNext: ${t.message}")
                _paymentResponse.value = t
            }
        }
    }
}