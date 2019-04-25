package com.example.lenovo.myproject.api

import android.os.Parcelable
import com.example.lenovo.myproject.DB.Student
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class StudentApi(
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
    val studentApis: List<StudentApi>
)