package com.example.lenovo.myproject

import android.app.Application
import android.arch.persistence.room.Room
import com.example.lenovo.myproject.DB.AppDB

class App : Application() {
    private lateinit var database: AppDB

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDB::class.java, "database").allowMainThreadQueries().build()
    }

    companion object {
        lateinit var instance: App
    }

    fun getDatabase(): AppDB {
        return database
    }
}