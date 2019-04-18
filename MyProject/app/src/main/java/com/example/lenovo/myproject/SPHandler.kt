package com.example.lenovo.myproject

import android.preference.PreferenceManager
import android.widget.TextView

object SPHandler {
    private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)

    fun setCookie(cookie: String?) {
        sharedPreferences.edit().putString("Cookie", cookie).apply()
    }

    fun getCookie(): String? {
        return sharedPreferences.getString("Cookie", "")
    }

    fun setPreferences(view: TextView?, keyWord: String) {
        sharedPreferences?.edit()?.putString(keyWord, view?.text.toString())?.apply()
    }

    fun setPreferences(data: String?, keyWord: String) {
        sharedPreferences?.edit()?.putString(keyWord, data)?.apply()
    }

    fun setData(view: TextView?, keyWord: String) {
        view?.text = sharedPreferences?.getString(keyWord, "")
    }

    fun isDataChanged(view: TextView?, keyWord: String): Boolean {
        return view?.text.toString() != sharedPreferences?.getString(keyWord, "")
    }
}