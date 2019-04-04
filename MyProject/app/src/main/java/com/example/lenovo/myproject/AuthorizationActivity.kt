package com.example.lenovo.myproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.api.Post
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

class AuthorizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        setupEntering()
    }

    private fun setupEntering() {
        findViewById<TextView>(R.id.enter).setOnClickListener {
            val email = findViewById<EditText>(R.id.login)
            val password = findViewById<EditText>(R.id.password)

            val postRequest = NetworkService.getInstance()?.post(Post(email.text.toString(), password.text.toString()))
            postRequest?.enqueue(object : retrofit2.Callback<Post> {
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Toast.makeText(applicationContext, "Can't sign in", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.code() != 200) {
                        Toast.makeText(applicationContext, "Не удалось совершить вход", Toast.LENGTH_SHORT)
                            .show()
                        return
                    } else {
                        Toast.makeText(applicationContext, "Вход успешно выполнен", Toast.LENGTH_SHORT)
                            .show()
                    }
                    val post = response.headers()
                    val cookie = post.get("Set-Cookie")
                    SPHandler.setCookie(cookie)
                }
            })
        }
    }
}