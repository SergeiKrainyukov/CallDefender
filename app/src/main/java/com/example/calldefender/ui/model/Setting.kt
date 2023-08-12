package com.example.calldefender.ui.model

sealed class Setting {
    class BlockUnfamiliarCallsSetting(val isChecked: Boolean) : Setting() {
        companion object {
            const val NAME = "BlockUnfamiliarCallsSetting"
        }
    }

    data object EmptySetting : Setting()
}