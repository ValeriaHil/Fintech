package com.example.lenovo.myproject.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TinkoffApi {
    @GET("user")
    fun user(@Header("Cookie") cookie: String?): Call<TinkoffResponse>

    @POST("signin")
    fun signin(@Body data: Post): Call<Post>
}