package com.example.lenovo.myproject.api

class TinkoffUserResponse(
    val user: User
)

class User(
    val first_name: String?,
    val last_name: String?,
    val middle_name: String?,
    val avatar: String?
) {
    constructor() : this("", "", "", "")
}

class Post(
    val email: String?,
    val password: String?
)