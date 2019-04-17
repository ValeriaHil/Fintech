package com.example.lenovo.myproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.lenovo.myproject.DB.*
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.api.Student
import com.example.lenovo.myproject.api.Students
import com.example.lenovo.myproject.fragments.StudentListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.util.*


class DetailsActivity : AppCompatActivity(), StudentListFragment.StudentListFragmentListener {
    companion object {
        private val logTag = DetailsActivity::class.java.name
        private val defaultComp = compareBy<Person> { it.scores }.thenByDescending { it.name }
        const val TIME_LIMIT = 10000
    }

    private val fragment = StudentListFragment.newInstance()
    private val receiver = (object : BroadcastReceiver() {
        val weakFragment = WeakReference<StudentListFragment>(fragment)

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(logTag, "onReceive")
            if (intent == null) {
                return
            }
            when (intent.action) {
                DBService.GET_STUDENTS -> {
                    val students = intent.getBundleExtra(DBHandler.RESULT)
                        .getParcelableArrayList<Person>(DBHandler.STUDENTS) ?: return
                    val refresh =
                        weakFragment.get()?.view?.findViewById<SwipeRefreshLayout>(R.id.swipe_students)
                    if (students.isEmpty()) {
                        refresh ?: return
                        onSwipeRefresh(refresh)
                    } else {
                        students.sortWith(defaultComp)
                        weakFragment.get()?.updateData(students.reversed())
                        refresh?.isRefreshing = false
                    }
                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setToolbar()
        supportFragmentManager.beginTransaction()
            .replace(R.id.recycler_container, fragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(DBService.GET_STUDENTS)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onStudentListCreated() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - DBHandler.timer > TIME_LIMIT) {
            onSwipeRefresh(findViewById(R.id.swipe_students))
        } else {
            DBHandler.getStudents(this)
        }
    }

    override fun onSwipeRefresh(refresh: SwipeRefreshLayout) {
        val getRequest = NetworkService.getInstance().getStudents(SPHandler.getCookie())
        val weakFragment = WeakReference<StudentListFragment>(fragment)
        val weakRefresh = WeakReference<SwipeRefreshLayout>(refresh)
        val currentTime = System.currentTimeMillis()
        val searcher = findViewById<SearchView>(R.id.search_view)
        searcher.setQuery("", false)
        if (currentTime - DBHandler.timer > TIME_LIMIT) {
            getRequest.enqueue(object : Callback<List<Students>> {
                override fun onFailure(call: Call<List<Students>>, t: Throwable) {
                }

                override fun onResponse(call: Call<List<Students>>, response: Response<List<Students>>) {
                    val students = response.body()?.get(1)?.students
                    val studentFragment = weakFragment.get()
                    if (students != null && studentFragment != null) {
                        val list = studentToPerson(students)
                        studentFragment.updateData(list.sortedWith(defaultComp).reversed())
                        DBHandler.addStudents(studentFragment.context!!, list)
                        DBHandler.timer = System.currentTimeMillis()
                    }
                    weakRefresh.get()?.isRefreshing = false
                }
            })
        } else {
            DBHandler.getStudents(this)
        }
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
            var students: List<Person>? = null
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

    private fun studentToPerson(students: List<Student>): List<Person> {
        val res = ArrayList<Person>()
        for (student in students) {
            res.add(Person(student))
        }
        return res
    }
}