package com.example.lenovo.myproject.lectures

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.lectures.tasks.TaskListFragment

class LecturesActivity : AppCompatActivity(), LecturesListFragment.LecturesListFragmentListener {
    override fun onLectureItemClicked(id: Int) {
        fragment = TaskListFragment.newInstance()
        fragment.arguments = Bundle()
        fragment.arguments?.putInt("ID", id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_lectures, fragment)
            .addToBackStack("tasks")
            .commit()
    }

    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lectures)
        loadFragment(LecturesListFragment.newInstance())
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (fragment is TaskListFragment) {
                supportFragmentManager.popBackStack()
                loadFragment(LecturesListFragment.newInstance())
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