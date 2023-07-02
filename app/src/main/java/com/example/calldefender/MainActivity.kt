package com.example.calldefender

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value

            if (isGranted) {
                // Разрешение получено
                when (permission) {
                    Manifest.permission.READ_PHONE_STATE -> {
                        // Разрешение на использование камеры получено
                    }

                    Manifest.permission.READ_CONTACTS -> {
                        // Разрешение на использование местоположения получено
                    }
                }
            } else {
                // Разрешение отклонено
                when (permission) {
                    Manifest.permission.READ_PHONE_STATE -> {
                        // Разрешение на использование камеры получено
                    }

                    Manifest.permission.READ_CONTACTS -> {
                        // Разрешение на использование местоположения получено
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsIfNecessary()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startCallScreeningPermissionScreen(REQUEST_ID_CALL_SCREENING)
        } else {
            startSelectDialerScreen(REQUEST_ID_SET_DEFAULT_DIALER)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID_CALL_SCREENING || requestCode == REQUEST_ID_SET_DEFAULT_DIALER) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                //...
            } else {
                //...
            }
        }
    }

    private fun requestPermissionsIfNecessary() {
        val missingPermissions = mutableListOf<String>()

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(permission)
            }
        }

        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            // Все необходимые разрешения уже получены
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startCallScreeningPermissionScreen(requestId: Int) {
        val roleManager = this.getSystemService(ROLE_SERVICE) as RoleManager
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        this.startActivityForResult(intent, requestId)
    }

    private fun startSelectDialerScreen(requestId: Int) {
        if (hasDialerCapability()) return
        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
        startActivityForResult(intent, requestId)
    }

    private fun hasDialerCapability(): Boolean {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        return packageName.equals(telecomManager.defaultDialerPackage)
    }

    companion object {
        const val REQUEST_ID_CALL_SCREENING = 9872
        const val REQUEST_ID_SET_DEFAULT_DIALER = 1144
    }
}
