package com.example.calldefender.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.calldefender.ui.model.Setting
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appContext: Context
) : SettingsRepository {
    override fun update(setting: Setting) {
        when (setting) {
            is Setting.BlockUnfamiliarCallsSetting -> updateBlockUnfamiliarCallsSetting(setting)
            Setting.EmptySetting -> {}
        }
    }

    override fun getSetting(name: String) = when (name) {
        Setting.BlockUnfamiliarCallsSetting.NAME -> getBlockUnfamiliarCallsSetting()
        else -> Setting.EmptySetting
    }


    private fun updateBlockUnfamiliarCallsSetting(blockUnfamiliarCallsSetting: Setting.BlockUnfamiliarCallsSetting) {
        appContext.getSharedPreferences(DEFAULT_PREFERENCES_NAME, MODE_PRIVATE).edit()
            .putBoolean(
                Setting.BlockUnfamiliarCallsSetting.NAME,
                blockUnfamiliarCallsSetting.isEnabled
            ).apply()
    }

    private fun getBlockUnfamiliarCallsSetting() =
        Setting.BlockUnfamiliarCallsSetting(
            appContext.getSharedPreferences(DEFAULT_PREFERENCES_NAME, MODE_PRIVATE)
                .getBoolean(
                    Setting.BlockUnfamiliarCallsSetting.NAME,
                    DEFAULT_BLOCK_UNFAMILIAR_CALLS_SETTING
                )
        )

    companion object {
        private const val DEFAULT_PREFERENCES_NAME = "call_defender_prefs"
        private const val DEFAULT_BLOCK_UNFAMILIAR_CALLS_SETTING = false
    }
}