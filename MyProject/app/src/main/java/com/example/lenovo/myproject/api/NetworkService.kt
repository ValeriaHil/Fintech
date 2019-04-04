package com.example.lenovo.myproject.api

import android.os.SystemClock
import com.example.lenovo.myproject.DB.Homework
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {
    private val retrofit: Retrofit
    private val tinkoffApi: TinkoffApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                SystemClock.sleep(2000)
                it.proceed(it.request())
            }
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
        tinkoffApi = retrofit.create(TinkoffApi::class.java)
    }

    fun getUser(cookie: String?): Call<TinkoffUserResponse> {
        return tinkoffApi.user(cookie)
    }

    fun post(post: Post): Call<Post> {
        return tinkoffApi.signin(post)
    }

    fun getLectures(cookie: String?): Call<Homework> {
        return tinkoffApi.lectures(cookie)
    }

    companion object {
        private var instance: NetworkService? = null
        const val HOST = "https://fintech.tinkoff.ru"
        const val BASE_URL = "$HOST/api/"

        fun getInstance(): NetworkService? {
            if (instance == null) {
                instance = NetworkService()
            }
            return instance
        }
    }
}