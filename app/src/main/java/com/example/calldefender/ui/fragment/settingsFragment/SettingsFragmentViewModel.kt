package com.example.calldefender.ui.fragment.settingsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.SettingUI
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settingsLiveData = MutableLiveData<List<SettingUI>>()
    val settingsLiveData: LiveData<List<SettingUI>>
        get() = _settingsLiveData

    fun init() {
        viewModelScope.launch {
            _settingsLiveData.value = settingsRepository.getAllSettings()
        }
    }

    fun updateSetting(settingUI: SettingUI) {
        viewModelScope.launch {
            settingsRepository.update(settingUI)
        }
    }
}