package com.example.lenovo.myproject.DB

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.example.lenovo.myproject.App

object DBHandler {
    const val LECTURE_ID = "LECTURE_ID"
    const val RESULT = "RESULT"
    const val TASKS = "TASKS"
    const val LECTURES = "LECTURES"
    const val STUDENTS = "STUDENTS"
    const val EXTRAS = "EXTRAS"
    val lectures = App.instance.getDatabase().lectureDao()
    val tasks = App.instance.getDatabase().taskDao()
    val students = App.instance.getDatabase().studentDao()
    var timer: Long = 0

    fun getTasks(context: Context, id: Int) {
        val extras = Bundle()
        extras.putInt(LECTURE_ID, id)
        startDBService(context, DBService.GET_TASKS, extras)
    }

    fun getLectures(context: Context) {
        startDBService(context, DBService.GET_LECTURES, Bundle())
    }

    fun getStudents(context: Context) {
        startDBService(context, DBService.GET_STUDENTS, Bundle())
    }

    fun addTasks(context: Context, tasks: List<Task>) {
        val extras = Bundle()
        extras.putParcelableArrayList(TASKS, ArrayList(tasks))
        startDBService(context, DBService.ADD_TASKS, extras)
    }

    fun addLectures(context: Context, lectures: List<Lecture>) {
        val extras = Bundle()
        extras.putParcelableArrayList(LECTURES, ArrayList(lectures))
        startDBService(context, DBService.ADD_LECTURES, extras)
    }

    fun addStudents(context: Context, students: List<Person>) {
        val extras = Bundle()
        extras.putParcelableArrayList(STUDENTS, ArrayList(students))
        startDBService(context, DBService.ADD_STUDENTS, extras)
    }

    private fun startDBService(context: Context, action: String, bundle: Bundle) {
        val intent = Intent(context, DBService::class.java)
        intent.action = action
        intent.putExtra(EXTRAS, bundle)
        context.startService(intent)
    }
}

class DBService : IntentService("DBService") {
    companion object {
        const val GET_TASKS = "GET_TASKS"
        const val GET_LECTURES = "GET_LECTURES"
        const val GET_STUDENTS = "GET_STUDENTS"
        const val ADD_LECTURES = "ADD_LECTURES"
        const val ADD_TASKS = "ADD_TASKS"
        const val ADD_STUDENTS = "ADD_STUDENTS"
        private val logTag = DBService::class.java.name
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }
        Log.d(logTag, "onHandleIntent")
        val resultIntent = Intent(intent.action)
        val extras = intent.getBundleExtra(DBHandler.EXTRAS)
        when (intent.action) {
            GET_TASKS -> {
                val tasks = DBHandler.tasks.getTasks(extras.getInt(DBHandler.LECTURE_ID))
                val bundle = Bundle()
                bundle.putParcelableArrayList(DBHandler.TASKS, ArrayList<Task>(tasks))
                resultIntent.putExtra(DBHandler.RESULT, bundle)
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
            }
            GET_LECTURES -> {
                val lectures = DBHandler.lectures.getAll()
                val bundle = Bundle()
                bundle.putParcelableArrayList(DBHandler.LECTURES, ArrayList<Lecture>(lectures))
                resultIntent.putExtra(DBHandler.RESULT, bundle)
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
            }
            GET_STUDENTS -> {
                val students = DBHandler.students.getAll()
                val bundle = Bundle()
                bundle.putParcelableArrayList(DBHandler.STUDENTS, ArrayList<Person>(students))
                resultIntent.putExtra(DBHandler.RESULT, bundle)
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
            }
            ADD_LECTURES -> {
                DBHandler.lectures.addAll(extras.getParcelableArrayList(DBHandler.LECTURES)!!)
            }
            ADD_TASKS -> {
                DBHandler.tasks.addAll(extras.getParcelableArrayList(DBHandler.TASKS)!!)
            }
            ADD_STUDENTS -> {
                DBHandler.students.addAll(extras.getParcelableArrayList(DBHandler.STUDENTS)!!)
            }
        }
    }

}