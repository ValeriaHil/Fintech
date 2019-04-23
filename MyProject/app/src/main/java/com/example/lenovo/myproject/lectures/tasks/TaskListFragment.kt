package com.example.lenovo.myproject.lectures.tasks

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Task
import com.example.lenovo.myproject.R
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment

class TaskListFragment : MvpLceFragment<SwipeRefreshLayout, List<Task>, TasksView, TasksPresenter>(),
    TasksView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recycler: RecyclerView
    private var adapter: Adapter = Adapter(emptyList())

    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lce, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.setTaskId(arguments?.getInt("ID")!!)
        contentView.setOnRefreshListener(this)
        loadData(false)
    }

    override fun onRefresh() {
        presenter.loadTasks()
    }

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
        presenter.loadTasks()
    }

    override fun createPresenter(): TasksPresenter {
        return TasksPresenter()
    }

    override fun setData(data: List<Task>?) {
        showContent()
        updateData(data)
        contentView.isRefreshing = false
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    private fun updateData(tasks: List<Task>?) {
        if (tasks != null) {
            adapter.updateData(tasks);
        }
    }

    inner class Adapter(private var tasks: List<Task>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.task_info, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p: Int) {
            holder.bind(tasks[p]);
        }

        fun updateData(tasks: List<Task>) {
            this.tasks = tasks
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val name: TextView = view.findViewById(R.id.task_info)

            fun bind(task: Task) {
                this.name.text = task.title
            }
        }
    }
}