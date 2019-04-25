package com.example.lenovo.myproject

import com.example.lenovo.myproject.DB.*
import com.example.lenovo.myproject.api.*
import com.example.lenovo.myproject.profile.ProfileRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebService {

    fun loadUser(network: NetworkService, profile: ProfileRepository) {
        val getRequest = network.getUser(SPHandler.getCookie())
        getRequest.enqueue(object : retrofit2.Callback<TinkoffUserResponse> {
            override fun onFailure(call: Call<TinkoffUserResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<TinkoffUserResponse>, response: Response<TinkoffUserResponse>) {
                val user = response.body()?.user
                profile.user.value = user
            }
        })
    }

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
                            newTasks.add(it.task)
                        }
                    }
                    tasksDB.addAllTasks(newTasks)
                }
            }
        })
    }

    fun loadStudents(network: NetworkService, database: StudentsStorage) {
        val getRequest = network.getStudents(SPHandler.getCookie())
        getRequest.enqueue(object : Callback<List<Students>> {
            override fun onFailure(call: Call<List<Students>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Students>>, response: Response<List<Students>>) {
                val students = response.body()?.get(1)?.studentApis
                if (students != null) {
                    val list = studentApiToStudent(students)
                    database.addAll(list)
                }
            }
        })
    }

    private fun studentApiToStudent(studentApis: List<StudentApi>): List<Student> {
        val res = java.util.ArrayList<Student>()
        for (student in studentApis) {
            res.add(Student(student))
        }
        return res
    }
}