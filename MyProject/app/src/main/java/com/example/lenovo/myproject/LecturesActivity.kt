package com.example.lenovo.myproject

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import com.example.lenovo.myproject.DB.Homework
import com.example.lenovo.myproject.DB.Task
import com.example.lenovo.myproject.api.NetworkService
import com.example.lenovo.myproject.fragments.LectureListFragment
import com.example.lenovo.myproject.fragments.TaskListFragment
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

class LecturesActivity : AppCompatActivity(), LectureListFragment.LectureListFragmentListener,
    TaskListFragment.TaskListFragmentListener {
    override fun onTaskListCreated(): List<Task>? {
        val database = App.instance.getDatabase()?.taskDao()
        if (database != null) {
            val tasks = database.getTasks(id)
            return tasks
        }
        return null
    }

    private lateinit var fragment: Fragment
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lectures)
        loadFragment(LectureListFragment.newInstance())
    }

    override fun onSwipeRefresh(refresh: SwipeRefreshLayout) {
        val getRequest = NetworkService.getInstance()?.getLectures(SPHandler.getCookie())
        val weakFragment = WeakReference<Fragment>(fragment)
        val weakRefresh = WeakReference<SwipeRefreshLayout>(refresh)
        getRequest?.enqueue(object : retrofit2.Callback<Homework> {
            override fun onFailure(call: Call<Homework>, t: Throwable) {

            }

            override fun onResponse(call: Call<Homework>, response: Response<Homework>) {
                val lectures = response.body()?.homeworks
                if (lectures != null) {
                    val lecturesFragment = weakFragment.get()
                    if (lecturesFragment != null && lecturesFragment is LectureListFragment) {
                        (lecturesFragment as LectureListFragment).updateData(lectures)
                    }
                    val database = App.instance.getDatabase()?.lectureDao()
                    val tasks = App.instance.getDatabase()?.taskDao()
                    val x = database?.addAll(lectures)
                    lectures.forEach { lecture ->
                        lecture.tasks?.forEach {
                            it.task.lectureId = lecture.id
                            tasks?.addAll(listOf(it.task))
                        }
                    }
                    weakRefresh.get()?.isRefreshing = false
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

    private fun loadFragment(newFragment: Fragment) {
        fragment = newFragment
        supportFragmentManager.beginTransaction().replace(R.id.container_lectures, fragment).commit()
    }
}