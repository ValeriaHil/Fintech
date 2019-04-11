package com.example.lenovo.myproject.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Person
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.customview.UserImage
import kotlin.random.Random

class StudentListFragment : Fragment() {
    companion object {
        fun newInstance(): StudentListFragment {
            return StudentListFragment()
        }
    }

    interface StudentListFragmentListener {
        fun onStudentListCreated()
        fun onSwipeRefresh(refresh: SwipeRefreshLayout)
    }

    private var listener: StudentListFragmentListener? = null
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is StudentListFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not StudentListFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = getView()?.findViewById(R.id.students_recycler_view) ?: return
        adapter = Adapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_students)
        refresh.setOnRefreshListener {
            listener?.onSwipeRefresh(refresh)
        }
        listener?.onStudentListCreated()
    }

    fun updateData(contacts: List<Person>) {
        adapter.updateData(contacts)
    }

    fun getStudents(): List<Person> {
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

    inner class Adapter(private var students: List<Person>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): Adapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            var layout = R.layout.contact_info_grid
            if (recycler.layoutManager !is GridLayoutManager) {
                layout = R.layout.contact_info
            }
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(contacts: List<Person>) {
            this.students = contacts
            notifyDataSetChanged()
        }

        fun getStudents(): List<Person> {
            return students
        }

        override fun getItemCount(): Int {
            return students.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, p: Int) {
            holder.bind(students[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val image: UserImage = view.findViewById(R.id.user_image)
            private val name: TextView = view.findViewById(R.id.name)
            private val scores: TextView = view.findViewById(R.id.scores)

            fun bind(student: Person) {
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