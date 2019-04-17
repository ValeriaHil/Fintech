package com.example.lenovo.myproject.DB

import android.arch.persistence.room.*
import android.os.Parcelable
import com.example.lenovo.myproject.api.Student
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Person(
    @PrimaryKey
    val id: Int,
    var name: String = "",
    var scores: String = "0"
) : Parcelable {
    constructor(student: Student) : this(student.id, student.name, student.grades.last().mark)
}


@Dao
interface PersonDao {
    @Query("SELECT * FROM Person")
    fun getAll(): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(students: List<Person>)
}