package com.example.calldefender.common

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

interface PermissionsController {
    fun registerActivityForRequestPermissions(activity: ComponentActivity)
    fun requestBasePermissions(activity: ComponentActivity, requiredPermissions: Array<String>)
    fun requestDialerPermission(activity: ComponentActivity)
    fun handleDialerPermissionResult(activity: ComponentActivity, requestCode: Int, resultCode: Int)
}

class PermissionControllerImpl : PermissionsController {

    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun registerActivityForRequestPermissions(activity: ComponentActivity) {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { entry ->
                val permission = entry.key
                val isGranted = entry.value
                if (!isGranted) {
                    //TODO: выводим диалог
                }
            }
        }
    }

    override fun requestBasePermissions(
        activity: ComponentActivity,
        requiredPermissions: Array<String>
    ) {
        val missingPermissions = mutableListOf<String>()
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(permission)
            }
        }
        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher?.launch(missingPermissions.toTypedArray())
        }
    }

    override fun requestDialerPermission(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startCallScreeningPermissionScreen(activity, REQUEST_ID_CALL_SCREENING)
        } else {
            startSelectDialerScreen(activity, REQUEST_ID_SET_DEFAULT_DIALER)
        }
    }

    override fun handleDialerPermissionResult(activity: ComponentActivity, requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_ID_CALL_SCREENING || requestCode == REQUEST_ID_SET_DEFAULT_DIALER) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                requestBasePermissions(
                    activity, arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS
                    )
                )
            } else {
                requestDialerPermission(activity)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startCallScreeningPermissionScreen(activity: ComponentActivity, requestId: Int) {
        val roleManager = activity.getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        activity.startActivityForResult(intent, requestId)
    }

    private fun startSelectDialerScreen(activity: ComponentActivity, requestId: Int) {
        if (hasDialerCapability(activity)) return
        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.packageName)
        activity.startActivityForResult(intent, requestId)
    }

    private fun hasDialerCapability(activity: ComponentActivity): Boolean {
        val telecomManager =
            activity.getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
        return activity.packageName.equals(telecomManager.defaultDialerPackage)
    }
}