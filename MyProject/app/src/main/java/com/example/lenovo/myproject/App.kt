package com.example.lenovo.myproject

import android.app.Application
import android.arch.persistence.room.Room
import com.example.lenovo.myproject.DB.AppDB
import com.example.lenovo.myproject.lectures.LecturesRepository
import com.example.lenovo.myproject.lectures.tasks.TasksRepository

class App : Application() {
    private lateinit var database: AppDB
    private lateinit var lecturesRepo: LecturesRepository
    private lateinit var tasksRepo: TasksRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDB::class.java, "database").allowMainThreadQueries().build()
        lecturesRepo = LecturesRepository()
        tasksRepo = TasksRepository()
    }

    companion object {
        lateinit var instance: App
    }

    fun getDatabase(): AppDB {
        return database
    }

    fun getLecturesRepository(): LecturesRepository {
        return lecturesRepo
    }

    fun getTasksRepository(): TasksRepository {
        return tasksRepo
    }
}