package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.lenovo.myproject.R

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<TextView>(R.id.tv_first_name)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_first_name_key), getString(R.string.default_first_name))
        activity?.findViewById<TextView>(R.id.tv_last_name)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_last_name_key), getString(R.string.default_last_name))
        activity?.findViewById<TextView>(R.id.tv_patronymic)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_patronymic_key), getString(R.string.default_patronymic))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        return v
    }
}