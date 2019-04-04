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

class SettingsFragment : Fragment() {
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    interface OnSettingItemSelected {
        fun onAuthorizationSelected()
    }

    private var listener: OnSettingItemSelected? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSettingItemSelected) {
            listener = context
        } else {
            throw ClassCastException("$context is not DialogSavingListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.authorization).setOnClickListener {
            listener?.onAuthorizationSelected()
        }

    }
}