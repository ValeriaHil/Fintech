package com.example.lenovo.myproject.students

import com.example.lenovo.myproject.App
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

class StudentsPresenter : MvpBasePresenter<StudentsView>() {
    private val repo = App.instance.studentsRepo

    fun loadStudents() {
        val students = repo.getStudents()
        students.observeForever {
            view?.setData(it)
        }
    }
}