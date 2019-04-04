package com.example.lenovo.myproject

import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView

class SPHandler {
    companion object {
        var sharedPreferences: SharedPreferences? = null

        fun setPreferences(context: Context) {
            sharedPreferences?.edit()?.putString(
                context.getString(R.string.preference_first_name_key),
                context.getString(R.string.default_first_name)
            )?.apply()
            sharedPreferences?.edit()?.putString(
                context.getString(R.string.preference_last_name_key),
                context.getString(R.string.default_last_name)
            )?.apply()
            sharedPreferences?.edit()?.putString(
                context.getString(R.string.preference_patronymic_key),
                context.getString(R.string.default_patronymic)
            )?.apply()
        }

        fun setCookie(cookie: String?) {
            sharedPreferences?.edit()?.putString("Cookie", cookie)?.apply()
        }

        fun getCookie(): String? {
            return sharedPreferences?.getString("Cookie", "")
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
}