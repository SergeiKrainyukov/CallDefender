package com.example.calldefender.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.calldefender.R
import com.example.calldefender.common.PermissionControllerImpl
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val permissionsController = PermissionControllerImpl()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragments()
        setPermissionsController()
    }

    private fun setPermissionsController() {
        permissionsController.registerActivityForRequestPermissions(this)
        permissionsController.requestDialerPermission(this)
    }

    private fun initFragments() {
        val callsFragment = CallsFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(callsFragment)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.callsItem -> setCurrentFragment(callsFragment)
                R.id.settingsItem -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionsController.handleDialerPermissionResult(this, requestCode, resultCode)
    }
}
