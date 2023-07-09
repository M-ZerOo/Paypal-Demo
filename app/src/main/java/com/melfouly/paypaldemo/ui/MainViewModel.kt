package com.melfouly.paypaldemo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melfouly.paypaldemo.model.AccessToken
import com.melfouly.paypaldemo.model.OrderRequest
import com.melfouly.paypaldemo.model.OrderResponse
import com.melfouly.paypaldemo.repository.Repository
import com.melfouly.paypaldemo.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImpl: Repository): ViewModel() {

    private val TAG = "ViewModel"

    private val _expirationTime = MutableLiveData<Int>()
    val expirationTime: LiveData<Int> get() = _expirationTime

    private lateinit var accessKey: String

    private val _orderResponse = MutableLiveData<OrderResponse>()
    val orderResponse: LiveData<OrderResponse> get() = _orderResponse

    private lateinit var accessTokenDisposable: Disposable
    private lateinit var orderResponseDisposable: Disposable

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
                _expirationTime.value = t.expiresIn
                accessKey = "Bearer ${t.token}"
                Log.d(TAG, "onNext: Expire: ${t.expiresIn}, Token: ${t.token}, TokenType: ${t.tokenType}")
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

    fun requestOrder(orderRequest: OrderRequest) {
        val orderResponseObservable = repositoryImpl.createOrder(accessKey, orderRequest)
        orderResponseObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(setOrderResponseObserver())
    }

    private fun  setOrderResponseObserver(): SingleObserver<OrderResponse> {
        return object : SingleObserver<OrderResponse> {
            override fun onSubscribe(d: Disposable) {
                orderResponseDisposable = d
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "setOrderResponseObserver onError: ${e.message}")
                Log.d(TAG, "setOrderResponseObserver onError: ${e.localizedMessage}")
            }

            override fun onSuccess(t: OrderResponse) {
                Log.d(TAG, "setOrderResponseObserver onSuccess: ID:${t.id}, Status:${t.status}")
                _orderResponse.value = t
            }
        }
    }

    override fun onCleared() {
        accessTokenDisposable.dispose()
        orderResponseDisposable.dispose()
        super.onCleared()
    }
}