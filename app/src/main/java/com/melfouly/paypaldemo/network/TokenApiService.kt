package com.melfouly.paypaldemo.network

import com.melfouly.paypaldemo.model.AccessToken
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.POST

interface TokenApiService {

    // Request auth token.
    @POST("v1/oauth2/token")
    fun requestToken(): Observable<AccessToken>
}