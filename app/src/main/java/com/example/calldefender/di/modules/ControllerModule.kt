package com.example.calldefender.di.modules

import android.content.Context
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.service.CallStatusController
import com.example.calldefender.service.CallStatusControllerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {
    @Provides
    @Singleton
    fun provideCallStatusController(
        settingsRepository: SettingsRepository,
        context: Context
    ): CallStatusController {
        return CallStatusControllerImpl(settingsRepository, context)
    }
}