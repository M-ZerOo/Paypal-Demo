package com.melfouly.paypaldemo.di

import android.util.Base64
import com.melfouly.paypaldemo.network.OrderApiService
import com.melfouly.paypaldemo.network.TokenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.sandbox.paypal.com/"
    const val CLIENT_ID =
        "YOUR_CLIENT_ID"
    const val SECRET_KEY =
        "YOUR_SECRET_KEY"


    @Singleton
    @Provides
    @Named("OrderInterceptor")
    fun provideOrderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    @Named("TokenInterceptor")
    fun provideTokenInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBody = FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build()

            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader(
                    "Authorization",
                    "Basic " + Base64.encodeToString(
                        "$CLIENT_ID:$SECRET_KEY".toByteArray(),
                        Base64.NO_WRAP
                    )
                )
                .post(requestBody)
                .build()
            chain.proceed(request)
        }
    }


    @Singleton
    @Provides
    @Named("OrderOkHttp")
    fun provideOrderOkHttpClient(@Named("OrderInterceptor") orderInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(orderInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("TokenOkHttp")
    fun provideTokenOkHttpClient(@Named("TokenInterceptor") tokenInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }


    @Singleton
    @Provides
    @Named("OrderRetrofit")
    fun provideOrderRetrofit(@Named("OrderOkHttp") orderOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(orderOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("TokenRetrofit")
    fun provideTokenRetrofit(@Named("TokenOkHttp") tokenOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(tokenOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOrderApiService(@Named("OrderRetrofit") orderRetrofit: Retrofit): OrderApiService {
        return orderRetrofit.create(OrderApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideTokenApiService(@Named("TokenRetrofit") tokenRetrofit: Retrofit): TokenApiService {
        return tokenRetrofit.create(TokenApiService::class.java)
    }
}