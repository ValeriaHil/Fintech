package com.example.lenovo.myproject.students

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.DB.Student
import com.example.lenovo.myproject.api.User
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class StudentsPresenter : MvpBasePresenter<StudentsView>() {
    private val repo = App.instance.studentsRepo
    private val userRepo = App.instance.userRepo

    fun loadStudents() {
        val result = Observable.combineLatest<List<Student>, User, List<Student>>(
            repo.getStudents(),
            userRepo.getUser(),
            BiFunction { students: List<Student>, user: User -> filterData(students, user) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<Student> ->
                view?.setData(list)
            },
                { error ->
                    view?.showError(error, false)
                })
    }

    private fun filterData(students: List<Student>, user: User): List<Student> {
        return students.filter { student -> student.getIntValueOfScores() > 20 }
            .map { student ->
                if (student.name == user.last_name + " " + user.first_name) {
                    student.name = "Вы"
                }
                student
            }
    }
}