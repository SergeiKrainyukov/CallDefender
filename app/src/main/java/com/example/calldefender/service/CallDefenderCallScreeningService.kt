package com.example.calldefender.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.UPDATE_CALLS_ACTION
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.common.parseCountryCode
import com.example.calldefender.common.removeTelPrefix
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

class CallDefenderCallScreeningService : CallScreeningService() {
    @Inject
    lateinit var repository: CallsRepository

    override fun onCreate() {
        super.onCreate()
        (application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle.toString().removeTelPrefix().parseCountryCode()
        var response = CallResponse.Builder()
        response = handlePhoneCall(response, phoneNumber)

        respondToCall(callDetails, response.build())
    }

    private fun handlePhoneCall(
        response: CallResponse.Builder,
        phoneNumber: String
    ): CallResponse.Builder {
        if (isInContacts(applicationContext, phoneNumber)) return response
        response.apply {
            setRejectCall(true)
            setDisallowCall(true)
            setSkipCallLog(false)
            addCallToRepository(phoneNumber, CallType.REJECTED)
        }
        updateUI()
        return response
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
        repository.addCall(
            CallUi(
                callNumber,
                Date().formatToPattern(DatePatterns.DEFAULT.pattern),
                callType
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun updateUI() {
        val intent = Intent(UPDATE_CALLS_ACTION)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}