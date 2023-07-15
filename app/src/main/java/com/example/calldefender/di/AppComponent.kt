package com.example.calldefender.di

import com.example.calldefender.di.modules.AppModule
import com.example.calldefender.di.modules.DataModule
import com.example.calldefender.di.modules.PermissionsControllerModule
import com.example.calldefender.di.modules.RepositoryModule
import com.example.calldefender.di.modules.ViewModelModule
import com.example.calldefender.service.CallDefenderCallScreeningService
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
        AppModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CallsFragment)
    fun inject(service: CallDefenderCallScreeningService)
}