package com.example.calldefender.repository

import com.example.calldefender.common.BLOCK_UNFAMILIAR_CALLS_SETTING_ID
import com.example.calldefender.data.SettingEntity
import com.example.calldefender.data.SettingEntityDao
import com.example.calldefender.ui.model.SettingUI
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingEntityDao: SettingEntityDao,
) : SettingsRepository {
    override suspend fun update(settingUI: SettingUI) =
        settingEntityDao.insert(SettingEntity.from(settingUI))

    override suspend fun getSetting(id: Int) = settingEntityDao.findByType(id)?.let { mapToUI(it) }

    override suspend fun getAllSettings() =
        settingEntityDao.getAll().map {
            mapToUI(it)
        }

    private fun mapToUI(settingEntity: SettingEntity): SettingUI {
        return when (settingEntity.id) {
            BLOCK_UNFAMILIAR_CALLS_SETTING_ID -> parseUnfamiliarSettingType(settingEntity)
            else -> SettingUI.EmptySetting
        }
    }


    private fun parseUnfamiliarSettingType(settingEntity: SettingEntity): SettingUI.BlockUnfamiliarCallsSettingUI {
        return SettingUI.BlockUnfamiliarCallsSettingUI(
            settingName = settingEntity.name,
            isEnabled = settingEntity.params.toBoolean()
        )
    }
}