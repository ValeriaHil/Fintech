package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R

class ProgressFragment : Fragment() {
    companion object {
        fun newInstance(): ProgressFragment {
            return ProgressFragment()
        }
    }

    interface ProgressFragmentListener {
        fun onProgressDetailsButtonClicked()
    }

    private var listener: ProgressFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProgressFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not ProgressFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailsButton = getView()?.findViewById<TextView>(R.id.tv_progress_details);
        detailsButton?.setOnClickListener {
            listener?.onProgressDetailsButtonClicked()
        }
    }
}