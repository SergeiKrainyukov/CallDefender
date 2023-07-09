package com.example.calldefender.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.calldefender.R
import com.example.calldefender.common.PermissionsController
import com.example.calldefender.databinding.ActivityMainBinding
import com.example.calldefender.di.MainComponent
import com.example.calldefender.ui.fragment.SettingsFragment
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var permissionsController: PermissionsController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        MainComponent.create().inject(this)
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
        val callsFragment = CallsFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(callsFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
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
