package com.example.lenovo.myproject.api

class TinkoffResponse {
    var user: User? = null
}

class User(
    val first_name: String?,
    val last_name: String?,
    val middle_name: String?,
    val avatar: String?
)

class Post(
    val email: String?,
    val password: String?
)