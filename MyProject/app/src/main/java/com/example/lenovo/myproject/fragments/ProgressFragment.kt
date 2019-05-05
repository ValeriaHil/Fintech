package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Student
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.customview.UserImage
import com.example.lenovo.myproject.students.StudentsPresenter
import com.example.lenovo.myproject.students.StudentsView
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment

class ProgressFragment : MvpLceFragment<ConstraintLayout, List<Student>, StudentsView, StudentsPresenter>(),
    StudentsView {
    companion object {
        fun newInstance(): ProgressFragment {
            return ProgressFragment()
        }
    }

    private lateinit var recycler: RecyclerView

    interface ProgressFragmentListener {
        fun onProgressDetailsButtonClicked()
        fun setRefreshing()
        fun dropRefreshing()
    }

    private var listener: ProgressFragmentListener? = null
    private lateinit var adapter: Adapter

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
        val detailsButton = view.findViewById<TextView>(R.id.tv_progress_details);
        detailsButton.setOnClickListener {
            listener?.onProgressDetailsButtonClicked()
        }
        recycler = getView()?.findViewById(R.id.recycler_for_progress) ?: return
        adapter = Adapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        loadData(false)
    }

    override fun loadData(pullToRefresh: Boolean) {
        listener?.setRefreshing()
        showLoading(pullToRefresh)
        presenter.loadStudents(pullToRefresh, 10)
    }

    override fun createPresenter(): StudentsPresenter {
        return StudentsPresenter()
    }

    override fun setData(data: List<Student>?) {
        showContent()
        updateData(data)
        listener?.dropRefreshing()
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    private fun updateData(data: List<Student>?) {
        if (data != null) {
            adapter.updateData(data)
        }
    }

    inner class Adapter(private var contacts: List<Student>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val layout = R.layout.person_in_progress_list
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(contacts: List<Student>) {
            this.contacts = contacts
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return contacts.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p: Int) {
            holder.bind(contacts[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val image: UserImage = view.findViewById(R.id.image)
            private val name: TextView = view.findViewById(R.id.name)
            private val scores: TextView = view.findViewById(R.id.badge_scores)

            fun bind(student: Student) {
                name.text = student.name
                scores.text = student.scores
                image.setInitials(UserImage.transform(student.name))
                image.setRoundColor(UserImage.getColor())
            }
        }
    }
}