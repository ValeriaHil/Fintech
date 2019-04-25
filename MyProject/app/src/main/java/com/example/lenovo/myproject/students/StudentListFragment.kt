package com.example.lenovo.myproject.students

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Student
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.customview.UserImage
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment
import kotlin.random.Random

class StudentListFragment : MvpLceFragment<SwipeRefreshLayout, List<Student>, StudentsView, StudentsPresenter>(),
    SwipeRefreshLayout.OnRefreshListener, StudentsView {

    private lateinit var recycler: RecyclerView
    private var adapter = Adapter(emptyList())

    companion object {
        fun newInstance(): StudentListFragment {
            return StudentListFragment()
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
        contentView.setOnRefreshListener(this)
        loadData(false)
    }

    override fun loadData(pullToRefresh: Boolean) {
        showLoading(pullToRefresh)
        presenter.loadStudents()
    }

    override fun createPresenter(): StudentsPresenter {
        return StudentsPresenter()
    }

    override fun setData(data: List<Student>?) {
        showContent()
        updateData(data)
        contentView.isRefreshing = false
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        return e?.message ?: ""
    }

    override fun onRefresh() {
        presenter.loadStudents()
    }

    fun updateData(contacts: List<Student>?) {
        if (contacts != null) {
            adapter.updateData(contacts)
        }
    }

    fun getStudents(): List<Student> {
        return adapter.getStudents()
    }

    fun changeElementsLocation() {
        if (recycler.layoutManager !is GridLayoutManager) {
            recycler.layoutManager = GridLayoutManager(context, 3)
        } else {
            recycler.layoutManager = LinearLayoutManager(context)
        }
        adapter = Adapter(adapter.getStudents())
        recycler.adapter = adapter
    }

    inner class Adapter(private var students: List<Student>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            var layout = R.layout.contact_info_grid
            if (recycler.layoutManager !is GridLayoutManager) {
                layout = R.layout.contact_info
            }
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(contacts: List<Student>) {
            this.students = contacts
            notifyDataSetChanged()
        }

        fun getStudents(): List<Student> {
            return students
        }

        override fun getItemCount(): Int {
            return students.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p: Int) {
            holder.bind(students[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val image: UserImage = view.findViewById(R.id.user_image)
            private val name: TextView = view.findViewById(R.id.name)
            private val scores: TextView = view.findViewById(R.id.scores)

            fun bind(student: Student) {
                this.name.text = student.name
                var dot = student.scores.indexOf(".")
                if (dot == -1) {
                    dot = student.scores.length
                }
                val intValue = student.scores.substring(0, dot).toInt()
                val suffix = " " + resources.getQuantityString(R.plurals.scores_plurals, intValue)
                val sc = "%.2f".format(student.scores.toDouble()) + suffix
                scores.text = sc
                image.setInitials(UserImage.transform(student.name))
                image.setRoundColor(Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
            }
        }
    }
}