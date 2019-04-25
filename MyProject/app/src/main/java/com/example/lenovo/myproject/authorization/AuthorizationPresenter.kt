package com.example.lenovo.myproject.authorization

import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.api.Post
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

class AuthorizationPresenter : MvpBasePresenter<AuthorizationView>() {

    interface Navigator {
        fun startMainActivity()
    }

    lateinit var navigator: WeakReference<Navigator>

    fun login(login: String, password: String) {
        val postRequest = NetworkService.getInstance().post(Post(login, password))
        postRequest.enqueue(object : retrofit2.Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                if (isViewAttached) {
                    view?.showError()
                }
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.code() != 200) {
                    if (isViewAttached) {
                        view?.showError()
                    }
                    return
                } else {
                    if (isViewAttached) {
                        view?.showOk()
                    }
                    val post = response.headers()
                    val cookie = post.get("Set-Cookie")
                    SPHandler.setCookie(cookie)
                    navigator.get()?.startMainActivity()
                }
            }
        })
    }
}