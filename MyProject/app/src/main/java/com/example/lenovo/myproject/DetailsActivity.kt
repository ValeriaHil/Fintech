package com.example.lenovo.myproject

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.lenovo.myproject.fragments.ContactListFragment

class DetailsActivity : AppCompatActivity() {
    companion object {
        private val logTag = DetailsActivity::class.java.name
        const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }

    private val fragment = ContactListFragment.newInstance()

    private val receiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, data: Intent?) {
            Log.d(logTag, "onReceive")
            val list = data?.getStringArrayListExtra("Contacts") ?: return
            fragment.updateData(list)
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
        val intentFilter = IntentFilter(Worker.ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, intentFilter)
        checkForPermission()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    doTask()
                } else {
                    finish()
                }
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = getString(R.string.title_progress)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        findViewById<TextView>(R.id.toolbar_button).setOnClickListener {
            fragment.changeElementsLocation()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            doTask()
        }
    }

    private fun doTask() {
        val intent = Intent(this, Worker::class.java)
        startService(intent)
    }
}