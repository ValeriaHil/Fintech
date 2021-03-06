package com.example.lenovo.myproject.DB

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

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
    fun getAll(): LiveData<List<Lecture>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(lectures: List<Lecture>)
}

@Parcelize
@Entity
data class Task(
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val title: String,
    var lectureId: Int,
    @SerializedName("max_score")
    val max_score: String,
    @SerializedName("deadline_date")
    val deadline: String?,
    var status: String,
    var mark: String
) : Parcelable

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE lectureId = :id")
    fun getTasks(id: Int): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(tasks: List<Task>)
}

class Homework {
    var homeworks: List<Lecture>? = null
}

class TaskResponse(
    val task: Task,
    val status: String,
    val mark: String
)