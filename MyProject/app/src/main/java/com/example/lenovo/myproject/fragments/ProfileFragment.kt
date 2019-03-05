package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.SPHandler

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
        SPHandler.setData(getView()?.findViewById(R.id.tv_first_name), getString(R.string.preference_first_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.tv_last_name), getString(R.string.preference_last_name_key))
        SPHandler.setData(getView()?.findViewById(R.id.tv_patronymic), getString(R.string.preference_patronymic_key))
        setupEditButton()
    }

    private fun setupEditButton() {
        val editButton = view?.findViewById<TextView>(R.id.tv_editing)
        editButton?.setOnClickListener {
            listener?.onEditProfileButtonClicked()
        }
    }
}