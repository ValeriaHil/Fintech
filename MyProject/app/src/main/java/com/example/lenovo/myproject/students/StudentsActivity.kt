package com.example.lenovo.myproject.students

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.lenovo.myproject.DB.*
import com.example.lenovo.myproject.R


class StudentsActivity : AppCompatActivity() {
    companion object {
        private val defaultComp = compareBy<Student> { it.scores }.thenByDescending { it.name }
    }

    private val fragment = StudentListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)
        setToolbar()
        supportFragmentManager.beginTransaction()
            .replace(R.id.recycler_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = getString(R.string.title_progress)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val spinner = findViewById<Spinner>(R.id.toolbar_button)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        val students = fragment.getStudents().sortedBy { it.name }
                        fragment.updateData(students)
                    }
                    2 -> {
                        val students = fragment.getStudents().sortedWith(defaultComp)
                        fragment.updateData(students.reversed())
                    }
                    3 -> {
                        fragment.changeElementsLocation()
                    }
                }
                spinner.setSelection(0)
            }
        }

        val searcher = findViewById<SearchView>(R.id.search_view)
        searcher.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var students: List<Student>? = null
            override fun onQueryTextChange(newText: String?): Boolean {
                if (students == null) {
                    students = fragment.getStudents()
                }
                fragment.updateData(students!!.filter { it -> newText.toString() in it.name })
                if (newText?.length == 0) {
                    students = null
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}