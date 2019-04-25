package com.example.lenovo.myproject.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        const val HOST = "https://fintech.tinkoff.ru"
        const val BASE_URL = "$HOST/api/"
    }

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                // SystemClock.sleep(2000)
                it.proceed(it.request())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(): TinkoffApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideClient())
            .build()
        return retrofit.create(TinkoffApi::class.java)
    }
}