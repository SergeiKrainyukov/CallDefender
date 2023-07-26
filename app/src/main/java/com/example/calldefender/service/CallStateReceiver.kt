package com.example.calldefender.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CallStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) && isInitialized) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.listen(
                CallDefenderPhoneStateListener(context),
                PhoneStateListener.LISTEN_CALL_STATE
            )
            isInitialized = true
        }
    }

    companion object {
        private var isInitialized = false
    }
}