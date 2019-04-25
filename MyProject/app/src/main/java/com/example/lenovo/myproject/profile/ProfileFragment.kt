package com.example.lenovo.myproject.profile

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
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
import com.example.lenovo.myproject.api.User
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : MvpLceFragment<SwipeRefreshLayout, User, ProfileView, ProfilePresenter>() {
    private lateinit var firstName: TextView

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
    }

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter()
    }

    override fun setData(data: User?) {
        firstName.text = data?.first_name
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstName = view.findViewById<TextView>(R.id.tv_first_name)
        presenter.getProfile()
    }
}