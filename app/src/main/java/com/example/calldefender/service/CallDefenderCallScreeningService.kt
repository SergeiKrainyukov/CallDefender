package com.example.calldefender.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.telecom.Call.Details
import android.telecom.CallScreeningService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.UPDATE_CALLS_ACTION
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.common.parseCountryCode
import com.example.calldefender.common.removeTelPrefix
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import com.example.calldefender.ui.model.SettingUI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

class CallDefenderCallScreeningService : CallScreeningService() {
    @Inject
    lateinit var callsRepository: CallsRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        (application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onScreenCall(callDetails: Details) {
        val phoneNumber = callDetails.handle.toString().removeTelPrefix().parseCountryCode()
        val response = CallResponse.Builder()
        disposables.add(settingsRepository.getAllSettings().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.forEach { settingUI ->
                    when (settingUI) {
                        is SettingUI.BlockUnfamiliarCallsSettingUI -> {
                            if (settingUI.isEnabled) {
                                if (!isInContacts(applicationContext, phoneNumber)) {
                                    rejectCall(callDetails, response, phoneNumber)
                                    updateUI()
                                    return@subscribe
                                }
                                respondToCall(callDetails, response.build())
                                updateUI()
                                return@subscribe
                            }
                            respondToCall(callDetails, response.build())
                            updateUI()
                            return@subscribe
                        }

                        else -> return@subscribe
                    }
                }
            }, {
                it.printStackTrace()
            })
        )
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

    private fun addCallToRepository(callNumber: String, callType: CallType) {
        callsRepository.addCall(
            CallUi(
                callNumber,
                Date().formatToPattern(DatePatterns.DEFAULT.pattern),
                callType
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun rejectCall(
        callDetails: Details,
        response: CallResponse.Builder,
        phoneNumber: String
    ) {
        val callResponse = response.apply {
            setRejectCall(true)
            setDisallowCall(true)
            setSkipCallLog(false)
        }.build()
        respondToCall(callDetails, callResponse)
        addCallToRepository(phoneNumber, CallType.REJECTED)
    }

    private fun updateUI() {
        val intent = Intent(UPDATE_CALLS_ACTION)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}