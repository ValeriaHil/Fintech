package com.example.lenovo.myproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import com.example.lenovo.myproject.DB.*
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.fragments.LectureListFragment
import com.example.lenovo.myproject.fragments.TaskListFragment
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

class LecturesActivity : AppCompatActivity(), LectureListFragment.LectureListFragmentListener,
    TaskListFragment.TaskListFragmentListener {
    private lateinit var fragment: Fragment
    private var id = 0

    private val receiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }
            when (intent.action) {
                DBService.GET_TASKS -> {
                    val tasks = intent.getBundleExtra(DBHandler.RESULT)
                        .getParcelableArrayList<Task>(DBHandler.TASKS) ?: return
                    if (fragment is TaskListFragment) {
                        val tasksFragment = fragment as TaskListFragment
                        tasksFragment.updateData(tasks)
                    }
                }
                DBService.GET_LECTURES -> {
                    if (fragment is LectureListFragment) {
                        val lecturesFragment = fragment as LectureListFragment
                        val lectures = intent.getBundleExtra(DBHandler.RESULT)
                            .getParcelableArrayList<Lecture>(DBHandler.LECTURES)
                        if (lectures == null || lectures.isEmpty()) {
                            val refresh = findViewById<SwipeRefreshLayout>(R.id.swipe_lectures)
                            onSwipeRefresh(refresh)
                        } else {
                            lecturesFragment.updateData(lectures)
                        }
                    }
                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lectures)
        loadFragment(LectureListFragment.newInstance())
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(DBService.GET_TASKS)
        intentFilter.addAction(DBService.GET_LECTURES)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(receiver)
        super.onStop()
    }

    override fun onSwipeRefresh(refresh: SwipeRefreshLayout) {
        val getRequest = NetworkService.getInstance().getLectures(SPHandler.getCookie())
        val weakFragment = WeakReference<Fragment>(fragment)
        val weakRefresh = WeakReference<SwipeRefreshLayout>(refresh)
        getRequest.enqueue(object : retrofit2.Callback<Homework> {
            override fun onFailure(call: Call<Homework>, t: Throwable) {

            }

            override fun onResponse(call: Call<Homework>, response: Response<Homework>) {
                val lectures = response.body()?.homeworks
                if (lectures != null) {
                    val lecturesFragment = weakFragment.get()
                    if (lecturesFragment != null && lecturesFragment is LectureListFragment) {
                        lecturesFragment.updateData(lectures)
                        DBHandler.addLectures(lecturesFragment.context!!, lectures)
                        val newTasks = ArrayList<Task>()
                        lectures.forEach { lecture ->
                            lecture.tasks?.forEach {
                                it.task.lectureId = lecture.id
                                newTasks.add(it.task)
                            }
                        }
                        DBHandler.addTasks(lecturesFragment.context!!, newTasks)
                        weakRefresh.get()?.isRefreshing = false
                    }
                }
            }
        })
    }

    override fun onItemClicked(id: Int) {
        this.id = id
        fragment = TaskListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_lectures, fragment)
            .addToBackStack("tasks")
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (fragment is TaskListFragment) {
                supportFragmentManager.popBackStack()
                loadFragment(LectureListFragment.newInstance())
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onTaskListCreated() {
        DBHandler.getTasks(this, id)
    }

    override fun onLectureListCreated() {
        DBHandler.getLectures(this)
    }


    private fun loadFragment(newFragment: Fragment) {
        fragment = newFragment
        supportFragmentManager.beginTransaction().replace(R.id.container_lectures, fragment).commit()
    }
}