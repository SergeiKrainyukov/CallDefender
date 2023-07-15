package com.example.calldefender

import android.app.Application
import com.example.calldefender.di.modules.AppModule
import com.example.calldefender.di.DaggerAppComponent

class CallDefenderApp : Application() {
    val appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
}