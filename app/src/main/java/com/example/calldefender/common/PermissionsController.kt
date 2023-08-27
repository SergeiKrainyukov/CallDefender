package com.example.calldefender.common

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import javax.inject.Inject

interface PermissionsController {
    fun getUnacceptedPermissions(): Array<String>
    fun isDialerApp(): Boolean
    fun createDialerPermissionIntent(): Intent
}

class PermissionControllerImpl @Inject constructor(
    private val applicationContext: Context
) : PermissionsController {

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
    )

    override fun getUnacceptedPermissions() = requiredPermissions.filter {
        ContextCompat.checkSelfPermission(
            applicationContext,
            it
        ) != PackageManager.PERMISSION_GRANTED
    }.toTypedArray()

    override fun isDialerApp() =
        applicationContext.packageName.equals((applicationContext.getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager).defaultDialerPackage)

    override fun createDialerPermissionIntent(): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager =
                applicationContext.getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
            return roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

        }
        return Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
            putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                applicationContext.packageName
            )
        }
    }
}