package com.example.lenovo.myproject.DB

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Lecture::class, Task::class, Student::class], version = 2, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun lectureDao(): LectureDao
    abstract fun taskDao(): TaskDao
    abstract fun studentDao(): PersonDao
}