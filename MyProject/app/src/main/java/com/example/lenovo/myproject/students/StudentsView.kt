package com.example.lenovo.myproject.students

import com.example.lenovo.myproject.DB.Student
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView

interface StudentsView : MvpLceView<List<Student>> {
}