package com.example.lenovo.myproject

import com.example.lenovo.myproject.DB.DatabaseModule
import com.example.lenovo.myproject.api.NetworkModule
import com.example.lenovo.myproject.authorization.AuthorizationActivity
import com.example.lenovo.myproject.authorization.AuthorizationPresenter
import com.example.lenovo.myproject.lectures.LecturesRepository
import com.example.lenovo.myproject.lectures.tasks.TasksRepository
import com.example.lenovo.myproject.profile.ProfileFragment
import com.example.lenovo.myproject.profile.ProfileRepository
import com.example.lenovo.myproject.students.StudentsActivity
import com.example.lenovo.myproject.students.StudentsRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DatabaseModule::class, NetworkModule::class])
@Singleton
interface RepositoryComponent {
    fun inject(activity: AuthorizationPresenter)
    fun inject(activity: StudentsActivity)
    fun inject(repo: LecturesRepository)
    fun inject(repo: TasksRepository)
    fun inject(repo: StudentsRepository)
    fun inject(repo: ProfileRepository)
    fun inject(o: ProfileFragment)
}