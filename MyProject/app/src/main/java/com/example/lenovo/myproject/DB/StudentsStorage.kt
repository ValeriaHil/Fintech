package com.example.lenovo.myproject.DB

import android.arch.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentsStorage {
    @Inject
    constructor()

    @Inject
    lateinit var database: AppDB

    fun getAll(): LiveData<List<Student>> {
        return database.studentDao().getAll()
    }

    fun addAll(students: List<Student>) {
        database.studentDao().addAll(students)
    }
}