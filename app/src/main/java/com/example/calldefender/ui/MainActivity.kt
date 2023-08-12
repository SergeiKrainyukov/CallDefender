package com.example.calldefender.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.common.PermissionsController
import com.example.calldefender.databinding.ActivityMainBinding
import com.example.calldefender.ui.fragment.settingsFragment.SettingsFragment
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionsController: PermissionsController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        (application as CallDefenderApp).appComponent.inject(this)
        setContentView(binding.root)
        initBottomNavigation()
        checkPermissions()
    }

    private fun checkPermissions() {
        with(permissionsController) {
            registerActivityForRequestPermissions(this@MainActivity)
            requestDialerPermission(this@MainActivity)
        }
    }

    private fun initBottomNavigation() {
        setCurrentFragment(CallsFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.callsItem -> setCurrentFragment(CallsFragment())
                R.id.settingsItem -> setCurrentFragment(SettingsFragment())
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
