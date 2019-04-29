package com.example.lenovo.myproject.lectures

import android.arch.lifecycle.LiveData
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.TasksStorage
import com.example.lenovo.myproject.DB.Lecture
import com.example.lenovo.myproject.DB.LecturesStorage
import com.example.lenovo.myproject.WebService
import com.example.lenovo.myproject.api.NetworkService
import javax.inject.Inject

class LecturesRepository {
    private var lectures: LiveData<List<Lecture>>

    @Inject
    lateinit var network: NetworkService

    @Inject
    lateinit var lecturesDB: LecturesStorage

    @Inject
    lateinit var tasksDB: TasksStorage

    init {
        App.instance.getRepositoryComponent().inject(this)
        lectures = lecturesDB.getAll()
    }

    fun getLectures(): LiveData<List<Lecture>> {
        refreshLectures()
        return lectures
    }

    private fun refreshLectures() {
        val lectures = lectures.value
        if (lectures == null || lectures.isEmpty()) {
            WebService.loadLectures(network, lecturesDB, tasksDB)
        }
    }
}