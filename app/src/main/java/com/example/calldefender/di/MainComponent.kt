package com.example.calldefender.di

import com.example.calldefender.ui.MainActivity
import dagger.Component

@Component(
    modules = [
        MainModule::class
    ]
)
interface MainComponent {
    fun inject(activity: MainActivity)

    companion object {
        fun create(): MainComponent {
            return DaggerMainComponent.builder().build()
        }
    }
}