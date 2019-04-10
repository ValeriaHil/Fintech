package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Task
import com.example.lenovo.myproject.R

class TaskListFragment : Fragment() {
    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }

    interface TaskListFragmentListener {
        fun onTaskListCreated(): List<Task>?
    }

    private var listener: TaskListFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TaskListFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not TaskListFragmentListener")
        }
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_for_tasks)
        val tasks = listener?.onTaskListCreated()
        if (tasks != null) {
            adapter = Adapter(tasks)
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    inner class Adapter(private var tasks: List<Task>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): Adapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.task_info, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, p: Int) {
            holder.bind(tasks[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val name: TextView = view.findViewById(R.id.task_info)

            fun bind(task: Task) {
                this.name.text = task.title
            }
        }
    }
}