package com.example.calldefender.service

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.SettingUI
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CallStatusControllerImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : CallStatusController {

    @SuppressLint("CheckResult")
    override fun checkPhone(phoneNumber: String): Observable<Boolean> {
        return settingsRepository
            .getAllSettings()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.just(checkAllSettings(it, phoneNumber))
            }.toObservable()
    }

    private fun checkAllSettings(settings: List<SettingUI>, phoneNumber: String): Boolean {
        var status = true
        settings.forEach { settingUI ->
            if (!status) return@forEach
            when (settingUI) {
                is SettingUI.BlockUnfamiliarCallsSettingUI -> {
                    status = checkSetting(settingUI, phoneNumber)
                }

                else -> {}
            }
        }
        return status
    }

    private fun checkSetting(
        setting: SettingUI.BlockUnfamiliarCallsSettingUI,
        phoneNumber: String
    ): Boolean {
        return !(setting.isEnabled && !isInContacts(context, phoneNumber))
    }

    @SuppressLint("Range")
    private fun isInContacts(context: Context, incomingNumber: String): Boolean {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                if (phoneNumber == incomingNumber) {
                    cursor.close()
                    return true
                }
            }
            cursor.close()
        }
        return false
    }
}