package com.example.lenovo.myproject

import android.content.Context
import com.example.lenovo.myproject.DB.*
import com.example.lenovo.myproject.api.*
import retrofit2.Call
import retrofit2.Response
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat.getSystemService


object WebService {

    fun loadLectures(network: NetworkService, lecturesDB: LecturesStorage, tasksDB: TasksStorage) {
        val getRequest = network.getLectures(SPHandler.getCookie())
        getRequest.enqueue(object : retrofit2.Callback<Homework> {
            override fun onFailure(call: Call<Homework>, t: Throwable) {

            }

            override fun onResponse(call: Call<Homework>, response: Response<Homework>) {
                val lectures = response.body()?.homeworks
                if (lectures != null) {
                    lecturesDB.addAll(lectures)
                    val newTasks = ArrayList<Task>()
                    lectures.forEach { lecture ->
                        lecture.tasks?.forEach {
                            it.task.lectureId = lecture.id
                            it.task.mark = it.mark
                            it.task.status = it.status
                            newTasks.add(it.task)
                        }
                    }
                    tasksDB.addAllTasks(newTasks)
                }
            }
        })
    }
}