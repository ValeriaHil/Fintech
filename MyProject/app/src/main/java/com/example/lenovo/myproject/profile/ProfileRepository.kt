package com.example.lenovo.myproject.profile

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.TinkoffApi
import com.example.lenovo.myproject.api.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileRepository {
    var user = User()

    @Inject
    lateinit var network: TinkoffApi

    init {
        App.instance.getRepositoryComponent().inject(this)
    }

    fun getUser(): Observable<User> {
        return getUserFromNet()
    }

    private fun getUserFromNet(): Observable<User> {
        return network.user(SPHandler.getCookie())
            .flatMap { userResponse -> Observable.fromCallable { userResponse.user } }
            .doOnNext { refreshUser(it) }
    }

    private fun refreshUser(it: User) {
        val result = Observable.fromCallable { this.user = it }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }
}