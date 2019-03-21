package com.example.lenovo.myproject

import android.app.IntentService
import android.content.Intent
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

class Worker : IntentService("Worker") {
    companion object {
        const val ACTION = "GET_CONTACTS"
        private val logTag = Worker::class.java.name
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(logTag, "onHandleIntent")
        val resultIntent = Intent(Worker.ACTION)
        resultIntent.putExtra("Contacts", getAll())
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
    }

    private fun getAll(): ArrayList<String> {
        val result = ArrayList<String>()
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            cursor?.use {
                val column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    val name = cursor.getString(column)
                    result.add(name)
                }
            }
        return result
    }
}