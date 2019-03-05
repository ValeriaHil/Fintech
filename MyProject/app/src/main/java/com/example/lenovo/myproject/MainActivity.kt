package com.example.lenovo.myproject

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.EditText
import com.example.lenovo.myproject.dialogs.DialogSaving
import com.example.lenovo.myproject.fragments.*

class MainActivity : AppCompatActivity(), ProfileFragment.ProfileListener,
    ProfileEditingFragment.ProfileEditingListener, DialogSaving.DialogSavingListener {

    companion object {
        const val ARG_MESSAGE = "ARG_MESSAGE"
    }

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
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(EventsFragment.newInstance())
        setToolbar()
        setPreferences()
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setPreferences() {
        SPHandler.sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        SPHandler.setPreferences(this)
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = resources.getString(R.string.title_events)
    }

    override fun onEditProfileButtonClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfileEditingFragment.newInstance())
            .addToBackStack("profile_editing")
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (!dataChanged()) {
                supportFragmentManager.popBackStack()
            } else {
                showSavingDialog()
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
}
