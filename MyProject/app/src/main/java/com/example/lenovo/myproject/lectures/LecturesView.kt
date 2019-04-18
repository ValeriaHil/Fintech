package com.example.lenovo.myproject.lectures

import com.example.lenovo.myproject.DB.Lecture
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView

interface LecturesView : MvpLceView<List<Lecture>> {
    fun onLectureItemClicked(id: Int)
}