package com.example.calldefender.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CallEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun callEntityDao(): CallEntityDao
}