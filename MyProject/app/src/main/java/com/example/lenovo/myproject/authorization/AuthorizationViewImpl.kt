package com.example.lenovo.myproject.authorization

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lenovo.myproject.R

class AuthorizationViewImpl(private val context: Context,
                            private val presenter: AuthorizationPresenter)
    : AuthorizationView {

    private val rootView: View = LayoutInflater.from(context).inflate(R.layout.activity_authorization, null)
    private val login: EditText
    private val password: EditText
    private val enter: TextView

    init {
        login = rootView.findViewById(R.id.login)
        password = rootView.findViewById(R.id.password)
        enter = rootView.findViewById(R.id.enter)
        enter.setOnClickListener {
            presenter.login()
        }
//        presenter.view = this

    }

    override fun getLogin(): String {
        return login.text.toString()
    }

    override fun getPassword(): String {
        return password.text.toString()
    }

    override fun showError() {
        Toast.makeText(context, "Не удалось совершить вход", Toast.LENGTH_SHORT)
            .show()
    }

    override fun showOk() {
        Toast.makeText(context, "Вход успешно выполнен", Toast.LENGTH_SHORT)
            .show()
    }

//    override fun getRootView(): View {
//        return rootView
//    }
}