package com.example.lenovo.myproject.profile

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.Post
import com.example.lenovo.myproject.api.TinkoffApi
import com.example.lenovo.myproject.api.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository {
    companion object {
        const val TIME_LIMIT = 100000;
    }

    private var user: User? = null
    private var timer: Long

    @Inject
    lateinit var network: TinkoffApi

    init {
        App.instance.getRepositoryComponent().inject(this)
        timer = 0
    }

    fun getUser(pullToRefresh: Boolean): Observable<User> {
        val currentTime = System.currentTimeMillis()
        if (user == null || pullToRefresh || currentTime - timer > TIME_LIMIT) {
            return getUserFromNet().onErrorResumeNext(Observable.fromCallable { return@fromCallable user })
        } else {
            return Observable.fromCallable { return@fromCallable user }
        }
    }

    fun setUser(data: Post): Observable<Response<Post>> {
        return network.signin(data).doOnNext { SPHandler.setCookie(it.headers().get("Set-Cookie")) }
    }

    private fun getUserFromNet(): Observable<User> {
        return network.user(SPHandler.getCookie())
            .map { userResponse -> userResponse.user }
            .doOnNext { refreshUser(it) }
    }

    private fun refreshUser(it: User) {
        val result = Observable.fromCallable { this.user = it }
            .doOnNext { timer = System.currentTimeMillis() }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }
}