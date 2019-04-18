package com.example.lenovo.myproject.lectures

import com.example.lenovo.myproject.App
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

class LecturesPresenter : MvpBasePresenter<LecturesView>() {
    private val repo: LecturesRepository = App.instance.getLecturesRepository()

    init {
        repo.getLectures().observeForever { it ->
            view?.setData(it)
        }
    }

    fun loadLectures() {
        val lectures = repo.getLectures()
        lectures.observeForever { it ->
            view?.setData(it)
        }
    }

    fun onItemClicked(id: Int) {
        view?.onLectureItemClicked(id)
    }
}