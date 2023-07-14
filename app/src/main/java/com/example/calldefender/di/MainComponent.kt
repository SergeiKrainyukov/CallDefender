package com.example.calldefender.di

import com.example.calldefender.ui.MainActivity
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import dagger.Component

@Component(
    modules = [
        MainModule::class,
        ViewModelModule::class
    ]
)
interface MainComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CallsFragment)

    companion object {
        fun create(): MainComponent {
            return DaggerMainComponent.builder().build()
        }
    }
}