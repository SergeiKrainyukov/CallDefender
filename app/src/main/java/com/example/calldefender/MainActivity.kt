package com.example.calldefender

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calldefender.common.PermissionControllerImpl

class MainActivity : AppCompatActivity() {

    private val permissionsController = PermissionControllerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionsController.registerActivityForRequestPermissions(this)
        permissionsController.requestDialerPermission(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionsController.handleDialerPermissionResult(this, requestCode, resultCode)
    }
}
