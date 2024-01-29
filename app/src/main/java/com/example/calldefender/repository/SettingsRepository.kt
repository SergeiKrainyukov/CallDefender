package com.example.calldefender.repository

import com.example.calldefender.ui.model.SettingUI

interface SettingsRepository {
    suspend fun update(settingUI: SettingUI)
    suspend fun getSetting(id: Int): SettingUI?
    suspend fun getAllSettings(): List<SettingUI>
}