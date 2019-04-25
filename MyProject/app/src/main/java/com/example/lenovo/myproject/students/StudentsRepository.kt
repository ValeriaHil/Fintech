package com.example.lenovo.myproject.students

import android.arch.lifecycle.LiveData
import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.Student
import com.example.lenovo.myproject.DB.StudentsStorage
import com.example.lenovo.myproject.WebService
import com.example.lenovo.myproject.api.NetworkService
import javax.inject.Inject

class StudentsRepository {
    private var students: LiveData<List<Student>>

    @Inject
    lateinit var database: StudentsStorage

    @Inject
    lateinit var network: NetworkService

    init {
        App.instance.getRepositoryComponent().inject(this)
        students = database.getAll()
    }

    fun getStudents(): LiveData<List<Student>> {
        refreshStudents()
        return students
    }

    private fun refreshStudents() {
        val students = students.value
        if (students == null || students.isEmpty()) {
            WebService.loadStudents(network, database)
        }
    }
}