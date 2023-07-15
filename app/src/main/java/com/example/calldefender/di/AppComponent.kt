package com.example.calldefender.di

import com.example.calldefender.ui.MainActivity
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        PermissionsControllerModule::class,
        ViewModelModule::class,
        DataModule::class,
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CallsFragment)
}