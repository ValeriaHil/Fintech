package com.example.lenovo.myproject.authorization

import android.view.View
import com.hannesdorfmann.mosby.mvp.MvpView

interface AuthorizationView : MvpView {
    fun getLogin(): String
    fun getPassword(): String
//    fun getRootView(): View
    fun showError()
    fun showOk()
}