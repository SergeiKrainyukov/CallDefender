package com.example.calldefender.di.modules

import android.content.Context
import com.example.calldefender.CallDefenderApp
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: CallDefenderApp) {

    @Provides
    fun provideAppContext(): Context {
        return app
    }

}