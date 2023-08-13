package com.example.calldefender.ui.model

sealed class SettingUI(val name: String) {
    class BlockUnfamiliarCallsSettingUI(val settingName: String, val isEnabled: Boolean) : SettingUI(settingName)
    data object EmptySetting: SettingUI("")
}