package com.example.lenovo.myproject

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.lenovo.myproject.dialogs.DialogSaving
import com.example.lenovo.myproject.fragments.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), ProfileFragment.ProfileListener,
    ProfileEditingFragment.ProfileEditingListener, DialogSaving.DialogSavingListener,
    ProgressFragment.ProgressFragmentListener, CoursesFragment.CoursesFragmentListener,
    SettingsFragment.OnSettingItemSelected {

    companion object {
        const val ARG_MESSAGE = "ARG_MESSAGE"
        const val GENERATE_SCORES = 1
    }

    private lateinit var currentFragment: Fragment
    private lateinit var receiver: BroadcastReceiver
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
            setProgressContacts(progressFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(EventsFragment.newInstance())
        setToolbar()
        setPreferences()
        setSettings()
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setSettings() {
        findViewById<ImageView>(R.id.settings).setOnClickListener {
            if (currentFragment is SettingsFragment) {
                return@setOnClickListener
            }
            currentFragment = SettingsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment)
                .addToBackStack("settings")
                .commit()
        }
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
        currentFragment = ProfileEditingFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, currentFragment)
            .addToBackStack("profile_editing")
            .commit()
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
            if (currentFragment is SettingsFragment) {
                supportFragmentManager.popBackStack()
                currentFragment = supportFragmentManager.fragments.last()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
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
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    override fun setProgressContacts(fragment: ProgressFragment) {
        val weakFragment = WeakReference<ProgressFragment>(fragment)
        val weakSwipe = WeakReference<SwipeRefreshLayout>(swipeRefreshLayout)

        receiver = (object : BroadcastReceiver() {
            override fun onReceive(context: Context?, data: Intent?) {
                Log.d("Main Activity", "onReceive")
                val list = data?.getStringArrayListExtra("Contacts") ?: return
                val handler = Handler(Handler.Callback {
                    if (it.what == GENERATE_SCORES) {
                        val scores = it.obj as List<*>
                        val contacts = ArrayList<Person>()
                        for (i in 0 until list.size) {
                            contacts.add(Person(list[i], scores[i] as String))
                        }
                        weakFragment.get()?.updateData(contacts)
                        weakSwipe.get()?.isRefreshing = false
                    }
                    true
                })
                Async.generateScores(list.size, handler)
                val localBroadcastManager = LocalBroadcastManager.getInstance(context!!)
                localBroadcastManager.unregisterReceiver(receiver)
            }
        })

        val intentFilter = IntentFilter(Worker.ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, intentFilter)
        checkForPermission()
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

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                DetailsActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            doTask()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            DetailsActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    doTask()
                } else {
                    Toast.makeText(this, "Can't load contacts", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun doTask() {
        val intent = Intent(this, Worker::class.java)
        startService(intent)
    }

    override fun onAuthorizationSelected() {
        val intent = Intent(this, AuthorizationActivity::class.java)
        startActivity(intent)
    }
}
