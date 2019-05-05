package com.example.lenovo.myproject.profile

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.api.NetworkModule
import com.example.lenovo.myproject.api.User
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment
import com.squareup.picasso.Picasso

class ProfileFragment : MvpLceFragment<SwipeRefreshLayout, User, ProfileView, ProfilePresenter>(), ProfileView {
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var avatar: ImageView

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
        presenter.loadProfile(pullToRefresh)
    }

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter()
    }

    override fun setData(data: User?) {
        showContent()
        contentView.isRefreshing = false

        Picasso.get().load(NetworkModule.HOST + data?.avatar).into(avatar)
        firstName.text = data?.first_name
        lastName.text = data?.last_name
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
        firstName = view.findViewById(R.id.tv_first_name)
        lastName = view.findViewById(R.id.tv_last_name)
        avatar = view.findViewById(R.id.profile_image)
        loadData(false)
    }
}