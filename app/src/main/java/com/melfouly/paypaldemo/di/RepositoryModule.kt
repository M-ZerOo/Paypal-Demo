package com.melfouly.paypaldemo.di

import com.melfouly.paypaldemo.network.OrderApiService
import com.melfouly.paypaldemo.network.TokenApiService
import com.melfouly.paypaldemo.repository.Repository
import com.melfouly.paypaldemo.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepo(
        orderApiService: OrderApiService,
        tokenApiService: TokenApiService
    ): Repository {
        return RepositoryImpl(orderApiService, tokenApiService)
    }
}