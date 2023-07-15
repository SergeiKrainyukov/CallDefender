package com.example.calldefender.di

import android.content.Context
import androidx.room.Room
import com.example.calldefender.data.AppDatabase
import com.example.calldefender.data.CallEntityDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideAppDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun getCallEntitiesDao(appDatabase: AppDatabase): CallEntityDao = appDatabase.callEntityDao()
}