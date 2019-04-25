package com.example.lenovo.myproject.api

import com.example.lenovo.myproject.DB.Homework
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService {

    @Inject
    constructor()

    @Inject
    lateinit var tinkoffApi: TinkoffApi

    fun getUser(cookie: String?): Call<TinkoffUserResponse> {
        return tinkoffApi.user(cookie)
    }

    fun post(post: Post): Call<Post> {
        return tinkoffApi.signin(post)
    }

    fun getLectures(cookie: String?): Call<Homework> {
        return tinkoffApi.lectures(cookie)
    }

    fun getStudents(cookie: String?): Call<List<Students>> {
        return tinkoffApi.students(cookie)
    }
}