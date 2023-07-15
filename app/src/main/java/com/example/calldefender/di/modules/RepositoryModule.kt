package com.example.calldefender.di.modules

import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.repository.CallsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideCallsRepository(repository: CallsRepositoryImpl): CallsRepository
}