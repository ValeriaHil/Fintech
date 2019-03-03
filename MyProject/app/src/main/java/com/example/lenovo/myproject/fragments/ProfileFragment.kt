package com.example.lenovo.myproject.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.SPHandler

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SPHandler.setData(getView()?.findViewById(R.id.tv_first_name), getString(R.string.preference_first_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.tv_last_name), getString(R.string.preference_last_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.tv_patronymic), getString(R.string.preference_patronymic_key))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}