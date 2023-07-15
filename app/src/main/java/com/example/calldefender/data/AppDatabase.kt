package com.example.calldefender.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calldefender.data.AppDatabase.Companion.DATABASE_VERSION

@Database(entities = [CallEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "CallDefenderDb"
        const val DATABASE_VERSION = 1
    }

    abstract fun callEntityDao(): CallEntityDao
}