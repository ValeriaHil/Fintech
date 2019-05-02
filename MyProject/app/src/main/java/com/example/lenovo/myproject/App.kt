package com.example.lenovo.myproject

import android.app.Application
import com.example.lenovo.myproject.DB.DatabaseModule
import com.example.lenovo.myproject.lectures.LecturesRepository
import com.example.lenovo.myproject.lectures.tasks.TasksRepository
import com.example.lenovo.myproject.profile.ProfileRepository
import com.example.lenovo.myproject.students.StudentsRepository

class App : Application() {
    private lateinit var lecturesRepo: LecturesRepository
    private lateinit var tasksRepo: TasksRepository
    lateinit var studentsRepo: StudentsRepository
    lateinit var userRepo: ProfileRepository
    private lateinit var repositoryComponent: RepositoryComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        lecturesRepo = LecturesRepository()
        tasksRepo = TasksRepository()
        studentsRepo = StudentsRepository()
        userRepo = ProfileRepository()
        SPHandler.setCookie("")
    }

    companion object {
        lateinit var instance: App
    }

    fun getRepositoryComponent(): RepositoryComponent {
        if (!this::repositoryComponent.isInitialized) {
            repositoryComponent = DaggerRepositoryComponent.builder().databaseModule(DatabaseModule(this)).build()
        }
        return repositoryComponent
    }

    fun getLecturesRepository(): LecturesRepository {
        return lecturesRepo
    }

    fun getTasksRepository(): TasksRepository {
        return tasksRepo
    }
}