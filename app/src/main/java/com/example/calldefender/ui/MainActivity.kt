package com.example.calldefender.ui

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.common.PermissionsController
import com.example.calldefender.databinding.ActivityMainBinding
import com.example.calldefender.ui.fragment.callsFragment.CallsFragment
import com.example.calldefender.ui.fragment.settingsFragment.SettingsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {}
    private val requestDialerPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != android.app.Activity.RESULT_OK) {
            requestDialerPermission()
        }
    }

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
            val unacceptedPermissions = getUnacceptedPermissions()
            if (unacceptedPermissions.isNotEmpty()) {
                requestPermissionLauncher.launch(unacceptedPermissions)
            }
            if (isDialerApp()) {
                return
            }
            requestDialerPermission()
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

    private fun requestDialerPermission() {
        requestDialerPermissionLauncher.launch(permissionsController.createDialerPermissionIntent())
    }
}
