package com.example.calldefender.repository

import com.example.calldefender.ui.model.SettingUI
import io.reactivex.Completable
import io.reactivex.Single

interface SettingsRepository {
    fun update(settingUI: SettingUI): Completable
    fun getSetting(id: Int): Single<SettingUI>
    fun getAllSettings(): Single<List<SettingUI>>
}