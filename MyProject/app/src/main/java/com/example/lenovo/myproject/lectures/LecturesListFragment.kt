package com.example.lenovo.myproject.lectures

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Lecture
import com.example.lenovo.myproject.R
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment

class LecturesListFragment : MvpLceFragment<SwipeRefreshLayout, List<Lecture>, LecturesView, LecturesPresenter>(),
    LecturesView, SwipeRefreshLayout.OnRefreshListener {

    override fun onLectureItemClicked(id: Int) {
        listener?.onLectureItemClicked(id)
    }

    companion object {
        fun newInstance(): LecturesListFragment {
            return LecturesListFragment()
        }
    }

    interface LecturesListFragmentListener {
        fun onLectureItemClicked(id: Int)
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter
    private var listener: LecturesListFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LecturesListFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not LecturesListFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lce, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler)
        adapter = Adapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.contentView)
        refresh.setOnRefreshListener(this)
        loadData(false)
    }

    private fun updateData(lectures: List<Lecture>?) {
        if (lectures != null) {
            adapter.updateData(lectures)
        }
    }

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
        presenter.loadLectures()
    }

    override fun createPresenter(): LecturesPresenter {
        return LecturesPresenter()
    }

    override fun setData(data: List<Lecture>?) {
        updateData(data)
        showContent()
        contentView.isRefreshing = false
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    override fun onRefresh() {
        presenter.loadLectures()
    }

    inner class Adapter(private var lectures: List<Lecture>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
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

        override fun onBindViewHolder(holder: ViewHolder, p: Int) {
            holder.bind(lectures[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val name: TextView = view.findViewById(R.id.lecture_info)
            private var id: Int = 0

            init {
                view.setOnClickListener {
                    presenter.onItemClicked(id)
                }
            }

            fun bind(lecture: Lecture) {
                this.name.text = lecture.title
                id = lecture.id
            }
        }
    }
}