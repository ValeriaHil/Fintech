package com.example.lenovo.myproject.api

import com.google.gson.annotations.SerializedName

data class TinkoffUserResponse(
    val user: User
)

data class User(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("first_name")
    val first_name: String = "",
    @SerializedName("last_name")
    val last_name: String = "",
    @SerializedName("middle_name")
    val middle_name: String?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone_mobile")
    val phone: String?,
    @SerializedName("skype_login")
    val skype: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("school")
    val school: String?,
    @SerializedName("university")
    val university: String?,
    @SerializedName("current_work")
    val work: String?
)

data class Post(
    val email: String?,
    val password: String?
)