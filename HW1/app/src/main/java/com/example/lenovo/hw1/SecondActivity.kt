package com.example.lenovo.hw1

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.View

class SecondActivity : AppCompatActivity() {

    companion object {
        private val logTag = SecondActivity::class.java.name
        const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }

    private val receiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(logTag, "onReceive")
            onFinish(Activity.RESULT_OK, intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(MyService.ACTION)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(receiver)
        super.onStop()
    }

    fun startMyService(view: View?) {
        Log.d(logTag, "Button clicked")
        checkForPermission()
    }

    fun onFinish(result : Int, intent: Intent?) {
        setResult(result, intent)
        finish()
    }

    private fun doTask() {
        val intent = Intent(this, MyService::class.java)
        startService(intent)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    doTask()
                } else {
                    onFinish(Activity.RESULT_CANCELED, Intent())
                }
            }
        }
    }
}
