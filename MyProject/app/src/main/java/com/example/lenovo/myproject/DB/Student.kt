package com.example.lenovo.myproject.DB

import android.arch.persistence.room.*
import android.os.Parcelable
import com.example.lenovo.myproject.api.StudentApi
import io.reactivex.Single
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

    fun getIntValueOfScores(): Int {
        var dot = scores.indexOf(".")
        if (dot == -1) {
            dot = scores.length
        }
        return scores.substring(0, dot).toInt()
    }
}


@Dao
interface PersonDao {
    @Query("SELECT * FROM Student")
    fun getAll(): Single<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(students: List<Student>)
}