package com.example.lenovo.myproject.authorization

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.api.Post
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthorizationPresenter : MvpBasePresenter<AuthorizationView>() {

    private val userRepo = App.instance.userRepo

    init {
        App.instance.getRepositoryComponent().inject(this)
    }

    fun login(login: String, password: String) {
        if (password.isEmpty() || login.isEmpty()
            || !android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()
        ) {
            view?.showError()
            return
        }
        val result = userRepo.setUser(Post(login, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code() == 200) {
                    run {
                        view?.showOk()
                        view?.disappear()
                    }
                } else {
                    view?.showError()
                }
            },
                { view?.showError() })
    }

    fun tryToEnter() {
        val result = userRepo.getUser(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null) {
                    view?.disappear()
                }
            }, { })
    }
}