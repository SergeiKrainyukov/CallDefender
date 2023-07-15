package com.example.calldefender.di

import com.example.calldefender.common.PermissionControllerImpl
import com.example.calldefender.common.PermissionsController
import dagger.Module
import dagger.Provides

@Module
class PermissionsControllerModule {
    @Provides
    fun providePermissionsController(): PermissionsController = PermissionControllerImpl()
}