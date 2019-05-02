package com.example.lenovo.myproject.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lenovo.myproject.MainActivity
import com.example.lenovo.myproject.R
import com.hannesdorfmann.mosby.mvp.MvpActivity

class AuthorizationActivity : MvpActivity<AuthorizationView, AuthorizationPresenter>(), AuthorizationView {
    override fun disappear() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authorization)
        val enter = findViewById<TextView>(R.id.enter)
        val login = findViewById<EditText>(R.id.login)
        val password = findViewById<EditText>(R.id.password)
        enter.setOnClickListener {
            presenter.login(login.text.toString(), password.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.tryToEnter()
    }

    override fun createPresenter(): AuthorizationPresenter {
        return AuthorizationPresenter()
    }

    override fun showError() {
        Toast.makeText(this, "Не удалось совершить вход", Toast.LENGTH_SHORT)
            .show()
    }

    override fun showOk() {
        Toast.makeText(this, "Вход успешно выполнен", Toast.LENGTH_SHORT)
            .show()
    }
}