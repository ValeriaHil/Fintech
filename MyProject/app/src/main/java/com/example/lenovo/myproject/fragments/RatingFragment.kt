package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R

class RatingFragment : Fragment() {
    companion object {
        fun newInstance(): RatingFragment {
            return RatingFragment()
        }
    }

    interface RatingFragmentListener {
        fun onRatingDetailsButtonClicked()
    }

    private var listener: RatingFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RatingFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not RatingFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailsButton = getView()?.findViewById<TextView>(R.id.tv_rating_details);
        detailsButton?.setOnClickListener {
            listener?.onRatingDetailsButtonClicked()
        }
    }
}