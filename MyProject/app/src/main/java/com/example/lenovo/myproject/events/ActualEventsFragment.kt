package com.example.lenovo.myproject.events

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.api.Event
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment

class ActualEventsFragment : MvpLceFragment<ConstraintLayout, List<Event>, EventsView, EventsPresenter>(), EventsView {
    private val adapter = Adapter(emptyList())
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actual_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler) ?: return
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter
        loadData(false)
    }

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
        presenter.loadEvents()
    }

    override fun createPresenter(): EventsPresenter {
        return EventsPresenter()
    }

    override fun setData(data: List<Event>?) {
        showContent()
        updateData(data)
    }

    private fun updateData(data: List<Event>?) {
        if (data != null) {
            adapter.updateData(data)
        }
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    companion object {
        fun newInstance(): ActualEventsFragment {
            return ActualEventsFragment()
        }
    }

    inner class Adapter(private var events: List<Event>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val layout = R.layout.actual_event_info
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(events: List<Event>) {
            this.events = events
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return events.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p: Int) {
            holder.bind(events[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val title: TextView = view.findViewById(R.id.title)

            fun bind(event: Event) {
                title.text = event.title
            }
        }
    }
}