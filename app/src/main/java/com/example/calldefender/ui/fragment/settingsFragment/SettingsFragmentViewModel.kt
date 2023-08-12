package com.example.calldefender.ui.fragment.settingsFragment

import androidx.lifecycle.ViewModel
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.Setting
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun init() {

    }

    fun updateSetting(setting: Setting) {
        settingsRepository.update(setting)
    }
}