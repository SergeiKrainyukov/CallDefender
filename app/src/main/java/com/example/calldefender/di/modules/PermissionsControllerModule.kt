package com.example.calldefender.di.modules

import com.example.calldefender.common.PermissionControllerImpl
import com.example.calldefender.common.PermissionsController
import dagger.Binds
import dagger.Module

@Module
abstract class PermissionsControllerModule {
    @Binds
    abstract fun providePermissionsController(permissionsController: PermissionControllerImpl): PermissionsController
}