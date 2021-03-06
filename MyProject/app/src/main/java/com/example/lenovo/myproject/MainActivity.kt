package com.example.lenovo.myproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.EditText
import com.example.lenovo.myproject.dialogs.DialogSaving
import com.example.lenovo.myproject.events.ActualEventsFragment
import com.example.lenovo.myproject.fragments.*
import com.example.lenovo.myproject.lectures.LecturesActivity
import com.example.lenovo.myproject.profile.ProfileFragment
import com.example.lenovo.myproject.fragments.ProgressFragment
import com.example.lenovo.myproject.students.StudentsActivity

class MainActivity : AppCompatActivity(),
    ProfileEditingFragment.ProfileEditingListener, DialogSaving.DialogSavingListener,
    ProgressFragment.ProgressFragmentListener, CoursesFragment.CoursesFragmentListener,
    RatingFragment.RatingFragmentListener, EventsFragment.EventsFragmentListener {

    override fun setRefreshing() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun dropRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {
        const val ARG_MESSAGE = "ARG_MESSAGE"
    }

    private lateinit var currentFragment: Fragment
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            supportActionBar?.title = item.title
            when (item.itemId) {
                R.id.events -> {
                    loadFragment(EventsFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.courses -> {
                    loadFragment(CoursesFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun loadFragment(fragment: Fragment) {
        currentFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onCoursesCreated() {
        val progressFragment = ProgressFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_0, progressFragment)
            .replace(R.id.fragment_1, RatingFragment.newInstance())
            .replace(R.id.fragment_2, PassedCoursesFragment.newInstance())
            .commit()
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            progressFragment.loadData(true)
        }
    }

    override fun onEventsCreated() {
        val actualEvents = ActualEventsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.actual_events, actualEvents)
            .commit()
        swipeRefreshLayout = findViewById(R.id.contentView)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            actualEvents.loadData(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(CoursesFragment.newInstance())
        setToolbar()
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.selectedItemId = R.id.courses
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = resources.getString(R.string.title_courses)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (currentFragment is ProfileEditingFragment) {
                if (!dataChanged()) {
                    supportFragmentManager.popBackStack()
                    currentFragment = supportFragmentManager.fragments.last()
                } else {
                    showSavingDialog()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveProfileEditingButtonClicked() {
        onSaveProfileEditingButtonClicked(R.id.te_first_name, R.string.preference_first_name_key)
        onSaveProfileEditingButtonClicked(R.id.te_last_name, R.string.preference_last_name_key)
        onSaveProfileEditingButtonClicked(R.id.te_patronymic, R.string.preference_patronymic_key)
        loadFragment(ProfileFragment.newInstance())
    }

    private fun onSaveProfileEditingButtonClicked(viewId: Int, stringId: Int) {
        val view = findViewById<EditText>(viewId)
        val keyWord = getString(stringId)
        SPHandler.setPreferences(view, keyWord)
    }

    override fun onCancelProfileEditingButtonClicked() {
        if (!dataChanged()) {
            loadFragment(ProfileFragment.newInstance())
        } else {
            showSavingDialog()
        }
    }

    private fun showSavingDialog() {
        val dialog = DialogSaving()
        val args = Bundle()
        args.putString(ARG_MESSAGE, getString(R.string.leave_message))
        dialog.arguments = args
        dialog.show(supportFragmentManager, null)
    }

    override fun onLeaveDialogSavingButtonClicked() {
        loadFragment(ProfileFragment.newInstance())
    }

    override fun onProgressDetailsButtonClicked() {
        val intent = Intent(this, StudentsActivity::class.java)
        startActivity(intent)
    }

    private fun dataChanged(): Boolean {
        return dataChanged(R.id.te_first_name, R.string.preference_first_name_key) ||
                dataChanged(R.id.te_last_name, R.string.preference_last_name_key) ||
                dataChanged(R.id.te_patronymic, R.string.preference_patronymic_key)
    }

    private fun dataChanged(viewId: Int, stringId: Int): Boolean {
        val view = findViewById<EditText>(viewId)
        val keyWord = getString(stringId)
        return SPHandler.isDataChanged(view, keyWord)
    }

    override fun onRatingDetailsButtonClicked() {
        val intent = Intent(this, LecturesActivity::class.java)
        startActivity(intent)
    }
}
