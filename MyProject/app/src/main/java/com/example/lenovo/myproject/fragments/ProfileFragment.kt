package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.api.TinkoffUserResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    interface ProfileListener {
        fun onEditProfileButtonClicked()
    }

    private var listener: ProfileListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProfileListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not DialogSavingListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEditButton()

        val getRequest = NetworkService.getInstance().getUser(SPHandler.getCookie())
        getRequest.enqueue(object : retrofit2.Callback<TinkoffUserResponse> {
            override fun onFailure(call: Call<TinkoffUserResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<TinkoffUserResponse>, response: Response<TinkoffUserResponse>) {
                val user = response.body()?.user
                if (user == null) {
                    if (context != null) {
                        Toast.makeText(
                            context,
                            "Авторизуйтесь для получения информации",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    return
                }
                getView()?.apply {
                    findViewById<TextView>(R.id.tv_first_name).text = user.first_name
                    findViewById<TextView>(R.id.tv_last_name).text = user.last_name
                    findViewById<TextView>(R.id.tv_patronymic).text = user.middle_name
                    val imageView = findViewById<ImageView>(R.id.profile_image)
                    Picasso.get().load(NetworkService.HOST + user.avatar).into(imageView)
                }
            }
        })
    }

    private fun setupEditButton() {
        val editButton = view?.findViewById<TextView>(R.id.tv_editing)
        editButton?.setOnClickListener {
            listener?.onEditProfileButtonClicked()
        }
    }
}