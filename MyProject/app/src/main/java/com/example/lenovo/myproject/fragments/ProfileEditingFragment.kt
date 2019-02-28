package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R

class ProfileEditingFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileEditingFragment {
            return ProfileEditingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<TextView>(R.id.te_first_name)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_first_name_key), getString(R.string.default_first_name))
        activity?.findViewById<TextView>(R.id.te_last_name)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_last_name_key), getString(R.string.default_last_name))
        activity?.findViewById<TextView>(R.id.te_patronymic)?.text = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.preference_patronymic_key), getString(R.string.default_patronymic))
    }
}