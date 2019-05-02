package com.example.lenovo.myproject.api

import com.example.lenovo.myproject.DB.Homework
import io.reactivex.Observable
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService {

    @Inject
    constructor()

    @Inject
    lateinit var tinkoffApi: TinkoffApi

//    fun post(post: Post): Observable<Post> {
//        return tinkoffApi.signin(post)
//    }

    fun getLectures(cookie: String?): Call<Homework> {
        return tinkoffApi.lectures(cookie)
    }
}