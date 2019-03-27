package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lenovo.myproject.R

class CoursesFragment : Fragment() {

    companion object {
        fun newInstance(): CoursesFragment {
            return CoursesFragment()
        }
    }

    interface CoursesFragmentListener {
        fun onCoursesCreated()
    }

    private var listener: CoursesFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CoursesFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not ProgressFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.onCoursesCreated()
    }
}