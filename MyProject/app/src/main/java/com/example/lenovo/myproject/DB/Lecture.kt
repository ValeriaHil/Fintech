package com.example.lenovo.myproject.DB

import android.arch.persistence.room.*
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Lecture(
    @PrimaryKey
    val id: Int,
    val title: String
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var tasks: List<TaskResponse>? = null
}

@Dao
interface LectureDao {
    @Query("SELECT * FROM lecture")
    fun getAll(): List<Lecture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(lectures: List<Lecture>)
}

@Parcelize
@Entity
data class Task(
    @PrimaryKey
    val id: Int,
    val title: String,
    var lectureId: Int
) : Parcelable

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE lectureId = :id")
    fun getTasks(id: Int): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(tasks: List<Task>)
}

class Homework {
    var homeworks: List<Lecture>? = null
}

class TaskResponse(
    val task: Task
)