package com.example.calldefender.di.modules

import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.repository.CallsRepositoryImpl
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideCallsRepository(repository: CallsRepositoryImpl): CallsRepository

    @Binds
    abstract fun provideSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}