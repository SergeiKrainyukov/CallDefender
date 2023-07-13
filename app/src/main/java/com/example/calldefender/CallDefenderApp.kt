package com.example.calldefender

import android.app.Application
import androidx.room.Room
import com.example.calldefender.data.AppDatabase

class CallDefenderApp : Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }

    fun getAppDatabase(): AppDatabase = database

    companion object {
        private const val DATABASE_NAME = "database"
    }
}