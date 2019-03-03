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
import com.example.lenovo.myproject.SPHandler

class ProfileEditingFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileEditingFragment {
            return ProfileEditingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SPHandler.setData(getView()?.findViewById(R.id.te_first_name), getString(R.string.preference_first_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.te_last_name), getString(R.string.preference_last_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.te_patronymic), getString(R.string.preference_patronymic_key))
    }
}