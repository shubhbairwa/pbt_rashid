package com.paybacktraders.paybacktraders.di

import com.paybacktraders.paybacktraders.api.PayBackTradersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(PayBackTradersApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()


    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): PayBackTradersApi =
        retrofit.create(PayBackTradersApi::class.java)
}