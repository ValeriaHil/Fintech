package com.example.lenovo.myproject.DB

import android.arch.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksStorage {
    @Inject
    constructor()

    @Inject
    lateinit var database: AppDB

    fun addAllTasks(tasks: List<Task>) {
        database.taskDao().addAll(tasks)
    }

    fun getTasks(id: Int): LiveData<List<Task>> {
        return database.taskDao().getTasks(id)
    }
}