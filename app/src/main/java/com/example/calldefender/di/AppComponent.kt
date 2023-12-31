package com.example.calldefender.di

import com.example.calldefender.di.modules.AppModule
import com.example.calldefender.di.modules.ControllerModule
import com.example.calldefender.di.modules.DataModule
import com.example.calldefender.di.modules.PermissionsControllerModule
import com.example.calldefender.di.modules.RepositoryModule
import com.example.calldefender.di.modules.ViewModelModule
import com.example.calldefender.service.CallDefenderCallScreeningService
import com.example.calldefender.service.CallDefenderPhoneStateListener
import com.example.calldefender.ui.MainActivity
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import com.example.calldefender.ui.fragment.settingsFragment.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        PermissionsControllerModule::class,
        ControllerModule::class,
        ViewModelModule::class,
        DataModule::class,
        AppModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CallsFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(service: CallDefenderCallScreeningService)
    fun inject(service: CallDefenderPhoneStateListener)
}