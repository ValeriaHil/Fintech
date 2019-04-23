package com.example.lenovo.myproject

import com.example.lenovo.myproject.DB.Homework
import com.example.lenovo.myproject.DB.Task
import com.example.lenovo.myproject.api.NetworkService
import retrofit2.Call
import retrofit2.Response

object WebService {
    fun loadLectures() {
        val getRequest = NetworkService.getInstance().getLectures(SPHandler.getCookie())
        getRequest.enqueue(object : retrofit2.Callback<Homework> {
            override fun onFailure(call: Call<Homework>, t: Throwable) {

            }

            override fun onResponse(call: Call<Homework>, response: Response<Homework>) {
                val lectures = response.body()?.homeworks
                if (lectures != null) {
                    App.instance.getDatabase().lectureDao().addAll(lectures)
                    val newTasks = ArrayList<Task>()
                    lectures.forEach { lecture ->
                        lecture.tasks?.forEach {
                            it.task.lectureId = lecture.id
                            newTasks.add(it.task)
                        }
                    }
                    App.instance.getDatabase().taskDao().addAll(newTasks)
                }
            }
        })
    }
}