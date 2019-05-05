package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lenovo.myproject.R

class EventsFragment : Fragment() {

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }

    interface EventsFragmentListener {
        fun onEventsCreated()
    }

    private var listener: EventsFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is EventsFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not EventsFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.onEventsCreated()
    }
}