package com.example.calldefender.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calldefender.common.BLOCK_UNFAMILIAR_CALLS_SETTING_ID
import com.example.calldefender.common.EMPTY_SETTING_ID
import com.example.calldefender.ui.model.SettingUI

@Entity
data class SettingEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "params") val params: String
) {
    companion object {
        fun from(settingUI: SettingUI): SettingEntity =
            when (settingUI) {
                is SettingUI.BlockUnfamiliarCallsSettingUI -> SettingEntity(
                    id = BLOCK_UNFAMILIAR_CALLS_SETTING_ID,
                    name = settingUI.settingName,
                    params = settingUI.isEnabled.toString()
                )

                else -> SettingEntity(
                    id = EMPTY_SETTING_ID,
                    name = "",
                    params = ""
                )
            }
    }
}