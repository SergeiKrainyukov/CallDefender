package com.example.calldefender.service

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.CallScreeningService
import com.example.calldefender.common.parseCountryCode
import com.example.calldefender.common.removeTelPrefix

class CallDefenderCallScreeningService : CallScreeningService() {

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
        }
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

}