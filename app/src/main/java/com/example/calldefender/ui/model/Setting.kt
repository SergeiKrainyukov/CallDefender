package com.example.calldefender.ui.model

sealed class Setting {
    class BlockUnfamiliarCallsSetting(val isEnabled: Boolean) : Setting() {
        companion object {
            const val NAME = "BlockUnfamiliarCallsSetting"
        }
    }

    data object EmptySetting : Setting()
}