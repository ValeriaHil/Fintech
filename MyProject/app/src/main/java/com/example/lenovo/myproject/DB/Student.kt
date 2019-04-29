package com.example.lenovo.myproject.DB

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.os.Parcelable
import com.example.lenovo.myproject.api.StudentApi
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Student(
    @PrimaryKey
    val id: Int,
    var name: String = "",
    var scores: String = "0"
) : Parcelable {
    constructor(studentApi: StudentApi) : this(studentApi.id, studentApi.name, studentApi.grades.last().mark)
}


@Dao
interface PersonDao {
    @Query("SELECT * FROM Student")
    fun getAll(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(students: List<Student>)
}