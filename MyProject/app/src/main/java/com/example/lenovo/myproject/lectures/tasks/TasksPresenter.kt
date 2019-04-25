package com.example.lenovo.myproject.lectures.tasks

import com.example.lenovo.myproject.App
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

class TasksPresenter : MvpBasePresenter<TasksView>() {
    private val repo = App.instance.getTasksRepository()

    fun setTaskId(id: Int) {
        repo.setTaskId(id)
    }

    fun loadTasks() {
        val tasks = repo.getTasks()
        tasks?.observeForever { it ->
            view?.setData(it)
        }
    }
}