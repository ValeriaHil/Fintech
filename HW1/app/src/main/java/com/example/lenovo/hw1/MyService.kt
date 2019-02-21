package com.example.lenovo.hw1

import android.app.IntentService
import android.content.Intent
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

class MyService : IntentService("Service") {
    companion object {
        const val ACTION = "GET_CONTACTS"
        private val logTag = MyService::class.java.name
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(logTag, "onHandleIntent")
        val resultIntent = Intent(MyService.ACTION)
        resultIntent.putExtra("Contacts", getAll())
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
    }

    private fun getAll() : ArrayList<String> {
        val result = ArrayList<String>()
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            while(cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                result.add(name)
            }
            cursor.close()
        }
        return result
    }
}