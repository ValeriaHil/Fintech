package com.example.lenovo.myproject.lectures.tasks

import android.arch.lifecycle.LiveData
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.TasksStorage
import com.example.lenovo.myproject.DB.Task
import javax.inject.Inject

class TasksRepository {
    private var id = 0
    private lateinit var tasks: LiveData<List<Task>>

    @Inject
    lateinit var database: TasksStorage

    init {
        App.instance.getRepositoryComponent().inject(this)
    }

    fun getTasks(): LiveData<List<Task>>? {
        return tasks
    }

    fun setTaskId(id: Int) {
        this.id = id
        tasks = database.getTasks(id)
    }
}