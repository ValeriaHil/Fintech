package com.example.lenovo.myproject.students

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.AppDB
import com.example.lenovo.myproject.DB.Student
import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.*
import javax.inject.Inject
import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import io.reactivex.schedulers.Schedulers

class StudentsRepository {
    companion object {
        const val TIME_LIMIT = 10000;
    }

    @Inject
    lateinit var database: AppDB

    @Inject
    lateinit var network: TinkoffApi

    private var timer: Long

    init {
        App.instance.getRepositoryComponent().inject(this)
        timer = 0
    }

    fun getStudents(): Observable<List<Student>> {
        val currentTime = System.currentTimeMillis()
        if (currentTime - timer > TIME_LIMIT) {
            return getStudentsFromNet()
        } else {
            return getStudentsFromDB()
        }
    }

    private fun getStudentsFromNet(): Observable<List<Student>> {
        return network.students(SPHandler.getCookie())
            .flatMap { students -> fromCallable { studentApiToStudent(students[1].studentApis) } }
            .doOnNext { storeInDB(it) }
            .doOnNext { timer = System.currentTimeMillis() }
    }

    private fun storeInDB(students: List<Student>) {
        val result = fromCallable { database.studentDao().addAll(students) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }

    private fun getStudentsFromDB(): Observable<List<Student>> {
        return database.studentDao().getAll().toObservable()
    }

    private fun studentApiToStudent(studentApis: List<StudentApi>): List<Student> {
        val res = java.util.ArrayList<Student>()
        for (student in studentApis) {
            res.add(Student(student))
        }
        return res
    }
}