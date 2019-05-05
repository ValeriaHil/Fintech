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
    private val studentsRepo = App.instance.studentsRepo
    private val userRepo = App.instance.userRepo

    fun loadStudents(pullToRefresh: Boolean) {
        val result = Observable.combineLatest<List<Student>, User, List<Student>>(
            studentsRepo.getStudents(pullToRefresh),
            userRepo.getUser(pullToRefresh),
            BiFunction { students: List<Student>, user: User -> findUser(students, user) })
        processObservable(result)
    }

    fun loadStudents(pullToRefresh: Boolean, amount: Long) {
        val result = Observable.combineLatest<List<Student>, User, List<Student>>(
            studentsRepo.getStudents(pullToRefresh),
            userRepo.getUser(pullToRefresh),
            BiFunction { students: List<Student>, user: User -> findUser(students, user) })
            .take(amount)
        processObservable(result)
    }

    private fun processObservable(observable: Observable<List<Student>>) {
        val result = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<Student> -> view?.setData(list) },
                { error -> view?.showError(error, false) })
    }

    private fun findUser(students: List<Student>, user: User): List<Student> {
        return students.filter { student -> student.getIntValueOfScores() > 20 }
            .map { student ->
                if (student.name == user.last_name + " " + user.first_name) {
                    student.name = "Вы"
                }
                student
            }
            .sortedBy { it.scores }
            .asReversed()
    }

}