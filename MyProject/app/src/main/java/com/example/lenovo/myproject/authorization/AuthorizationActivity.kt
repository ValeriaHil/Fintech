package com.example.lenovo.myproject.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lenovo.myproject.MainActivity
import com.example.lenovo.myproject.R
import com.hannesdorfmann.mosby.mvp.MvpActivity
import java.lang.ref.WeakReference

class AuthorizationActivity : MvpActivity<AuthorizationView, AuthorizationPresenter>(), AuthorizationView,
    AuthorizationPresenter.Navigator {
    override fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authorization)
        presenter.navigator = WeakReference(this)
        val enter = findViewById<TextView>(R.id.enter)
        val login = findViewById<EditText>(R.id.login)
        val password = findViewById<EditText>(R.id.password)
        enter.setOnClickListener {
            presenter.login(login.text.toString(), password.text.toString())
        }
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