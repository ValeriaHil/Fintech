package com.example.lenovo.myproject.DB

import android.arch.persistence.room.*

@Entity
class Lecture(
    @PrimaryKey
    val id: Int,
    val title: String
) {
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

@Entity
class Task(
    @PrimaryKey
    val id: Int,
    val title: String,
    var lectureId: Int
)

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