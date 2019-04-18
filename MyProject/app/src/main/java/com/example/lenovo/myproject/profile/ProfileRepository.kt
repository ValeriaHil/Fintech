package com.example.lenovo.myproject.profile

import android.arch.lifecycle.MutableLiveData
import com.example.lenovo.myproject.api.User

class ProfileRepository {
    val user = MutableLiveData<User>()
}