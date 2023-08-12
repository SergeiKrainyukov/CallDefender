package com.example.calldefender.repository

import com.example.calldefender.ui.model.Setting

interface SettingsRepository {
    fun update(setting: Setting)
    fun getSetting(name: String): Setting
}