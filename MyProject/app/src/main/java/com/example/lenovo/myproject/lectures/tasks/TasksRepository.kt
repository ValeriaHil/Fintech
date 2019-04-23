package com.example.lenovo.myproject.lectures.tasks

import android.arch.lifecycle.LiveData
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.Task

class TasksRepository {
    private var id = 0
    private val tasksDao = App.instance.getDatabase().taskDao()
    private lateinit var tasks: LiveData<List<Task>>

    fun getTasks(): LiveData<List<Task>> {
        return tasks
    }

    fun setTaskId(id: Int) {
        this.id = id
        tasks = tasksDao.getTasks(id)
    }
}