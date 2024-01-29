package com.example.calldefender.service

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.SettingUI
import javax.inject.Inject

class CallStatusControllerImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : CallStatusController {

    override suspend fun checkPhone(phoneNumber: String) =
        checkAllSettings(settingsRepository.getAllSettings(), phoneNumber)

    private fun checkAllSettings(settings: List<SettingUI>, phoneNumber: String) =
        settings.map {
            if (it is SettingUI.BlockUnfamiliarCallsSettingUI) checkSetting(
                it,
                phoneNumber
            ) else true
        }.find { !it } ?: true


    private fun checkSetting(
        setting: SettingUI.BlockUnfamiliarCallsSettingUI,
        phoneNumber: String
    ) = !(setting.isEnabled && !isInContacts(context, phoneNumber))


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
        if (cursor == null || cursor.count <= 0) return false
        while (cursor.moveToNext()) {
            val phoneNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            if (phoneNumber == incomingNumber) {
                cursor.close()
                return true
            }
        }
        cursor.close()
        return false
    }
}