package com.example.lenovo.myproject.api

import com.example.lenovo.myproject.DB.Homework
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TinkoffApi {
    @GET("user")
    fun user(@Header("Cookie") cookie: String?): Observable<TinkoffUserResponse>

    @GET("course/android_spring_2019/homeworks")
    fun lectures(@Header("Cookie") cookie: String?): Call<Homework>

    @POST("signin")
    fun signin(@Body data: Post): Observable<Response<Post>>

    @GET("course/android_spring_2019/grades")
    fun students(@Header("Cookie") cookie: String?): Observable<List<Students>>

    @GET("calendar/list/event")
    fun events(@Header("Cookie") cookie: String?): Observable<Events>
}