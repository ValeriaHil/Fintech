package com.example.lenovo.myproject.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Student(
    @SerializedName("student_id")
    val id: Int,
    @SerializedName("student")
    val name: String,
    @SerializedName("grades")
    val grades: List<Grade>
) : Parcelable

@Parcelize
data class Grade(
    @SerializedName("mark")
    val mark: String
) : Parcelable

data class Students(
    @SerializedName("grades")
    val students: List<Student>
)