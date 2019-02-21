package com.example.lenovo.hw1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        const val CONTACT_VIEW = 1
        private val logTag = MainActivity::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchSecondActivity(view: View?) {
        Log.d(logTag, "Button clicked")
        val intent = Intent(this, SecondActivity::class.java)
        startActivityForResult(intent, CONTACT_VIEW)
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_VIEW) {
            if (resultCode == Activity.RESULT_OK) {
                val list = data?.getStringArrayListExtra("Contacts")
                val lw = findViewById<ListView>(R.id.list_view)
                lw.adapter  = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
            } else {
                val toast = Toast.makeText(this, "Failed. Try to allow this app reading contacts", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }
}
