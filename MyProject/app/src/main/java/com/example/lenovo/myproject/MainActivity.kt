package com.example.lenovo.myproject

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.View
import android.widget.EditText
import com.example.lenovo.myproject.dialogs.DialogSaving
import com.example.lenovo.myproject.fragments.*

class MainActivity : AppCompatActivity() {

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
        getPreferences(Context.MODE_PRIVATE).edit()
            .putString(getString(R.string.preference_first_name_key), getString(R.string.default_first_name)).apply()
        getPreferences(Context.MODE_PRIVATE).edit()
            .putString(getString(R.string.preference_last_name_key), getString(R.string.default_last_name)).apply()
        getPreferences(Context.MODE_PRIVATE).edit()
            .putString(getString(R.string.preference_patronymic_key), getString(R.string.default_patronymic)).apply()
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = resources.getString(R.string.title_events)
    }

    fun onEditButtonClicked(view: View?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfileEditingFragment.newInstance())
            .addToBackStack("profile_editing")
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (!checkChanges()) {
                supportFragmentManager.popBackStack()
            } else {
                showSavingDialog()
            }
        } else {
            super.onBackPressed()
        }
    }

    fun onSaveButtonClicked(view: View?) {
        getPreferences(Context.MODE_PRIVATE).edit().putString(
            getString(R.string.preference_first_name_key),
            findViewById<EditText>(R.id.te_first_name).text.toString()
        ).apply()
        getPreferences(Context.MODE_PRIVATE).edit().putString(
            getString(R.string.preference_last_name_key),
            findViewById<EditText>(R.id.te_last_name).text.toString()
        ).apply()
        getPreferences(Context.MODE_PRIVATE).edit().putString(
            getString(R.string.preference_patronymic_key),
            findViewById<EditText>(R.id.te_patronymic).text.toString()
        ).apply()
        loadFragment(ProfileFragment.newInstance())
    }

    fun onCancelButtonClicked(view: View?) {
        if (!checkChanges()) {
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

    private fun checkChanges(): Boolean {
        if (findViewById<EditText>(R.id.te_first_name).text.toString() != getPreferences(Context.MODE_PRIVATE)?.getString(
                getString(R.string.preference_first_name_key),
                getString(R.string.default_first_name)
            )
        ) {
            return true
        }
        if (findViewById<EditText>(R.id.te_last_name).text.toString() != getPreferences(Context.MODE_PRIVATE)?.getString(
                getString(R.string.preference_last_name_key),
                getString(R.string.default_last_name)
            )
        ) {
            return true
        }
        if (findViewById<EditText>(R.id.te_patronymic).text.toString() != getPreferences(Context.MODE_PRIVATE)?.getString(
                getString(R.string.preference_patronymic_key),
                getString(R.string.default_patronymic)
            )
        ) {
            return true
        }
        return false
    }
}
