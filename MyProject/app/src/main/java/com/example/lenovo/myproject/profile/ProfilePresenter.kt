package com.example.lenovo.myproject.profile

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.api.User
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilePresenter : MvpBasePresenter<ProfileView>() {
    private val repo = App.instance.userRepo

    fun loadProfile(pullToRefresh: Boolean) {
        val result = repo.getUser(pullToRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user: User -> view?.setData(user) },
                { error -> view?.showError(error, false) })
    }
}