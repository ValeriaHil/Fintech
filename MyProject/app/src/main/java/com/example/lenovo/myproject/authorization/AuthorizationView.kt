package com.example.lenovo.myproject.authorization

import com.hannesdorfmann.mosby.mvp.MvpView

interface AuthorizationView : MvpView {
    fun showError()
    fun showOk()
}