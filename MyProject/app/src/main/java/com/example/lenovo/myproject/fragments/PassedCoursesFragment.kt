package com.example.lenovo.myproject.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lenovo.myproject.R

class PassedCoursesFragment: Fragment() {
    companion object {
        fun newInstance(): PassedCoursesFragment {
            return PassedCoursesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_passed_courses, container, false)
    }
}