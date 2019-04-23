package com.example.lenovo.myproject.lectures.tasks

import com.example.lenovo.myproject.DB.Task
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView

interface TasksView : MvpLceView<List<Task>> {
}