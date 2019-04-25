package com.example.lenovo.myproject.DB

import android.arch.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LecturesStorage {
    @Inject
    constructor()

    @Inject
    lateinit var database: AppDB

    fun getAll(): LiveData<List<Lecture>> {
        return database.lectureDao().getAll()
    }

    fun addAll(lectures: List<Lecture>) {
        database.lectureDao().addAll(lectures)
    }
}