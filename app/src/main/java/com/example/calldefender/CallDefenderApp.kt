package com.example.calldefender

import android.app.Application
import com.example.calldefender.di.AppModule
import com.example.calldefender.di.DaggerAppComponent

class CallDefenderApp : Application() {
    val appComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
}