package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.Lecture
import com.example.lenovo.myproject.R

class LectureListFragment : Fragment() {
    companion object {
        fun newInstance(): LectureListFragment {
            return LectureListFragment()
        }
    }

    interface LectureListFragmentListener {
        fun onSwipeRefresh(refresh: SwipeRefreshLayout)
        fun onItemClicked(id: Int)
        fun onLectureListCreated()
    }

    private var listener: LectureListFragmentListener? = null
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LectureListFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not LectureListFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_for_lectures)
        adapter = Adapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_lectures)
        refresh.setOnRefreshListener {
            listener?.onSwipeRefresh(refresh)
        }

        listener?.onLectureListCreated()
    }

    fun updateData(lectures: List<Lecture>) {
        adapter.updateData(lectures)
    }

    inner class Adapter(private var lectures: List<Lecture>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): Adapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.lecture_info, parent, false)
            return ViewHolder(view)
        }

        fun updateData(lectures: List<Lecture>) {
            this.lectures = lectures
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return lectures.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, p: Int) {
            holder.bind(lectures[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val name: TextView = view.findViewById(R.id.lecture_info)
            private var id: Int = 0

            init {
                view.setOnClickListener {
                    listener?.onItemClicked(id)
                }
            }

            fun bind(lecture: Lecture) {
                this.name.text = lecture.title
                id = lecture.id
            }
        }
    }
}