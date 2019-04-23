package com.example.lenovo.myproject.lectures

import android.arch.lifecycle.LiveData
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.Lecture
import com.example.lenovo.myproject.WebService

class LecturesRepository {
    private val lectureDao= App.instance.getDatabase().lectureDao()
    private val lectures = lectureDao.getAll()

    fun getLectures(): LiveData<List<Lecture>> {
        refreshLectures()
        return lectures
    }

    private fun refreshLectures() {
        val lectures = lectures.value
        if (lectures == null || lectures.isEmpty()) {
            WebService.loadLectures()
        }
    }
}