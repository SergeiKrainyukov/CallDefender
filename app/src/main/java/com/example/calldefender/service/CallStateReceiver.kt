package com.example.calldefender.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CallStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) && telephonyManager == null) {
            telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            phoneStateListener = CallDefenderPhoneStateListener(context)
            telephonyManager?.listen(
                phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE
            )
        }
    }

    companion object {
        var phoneStateListener: PhoneStateListener? = null
        var telephonyManager: TelephonyManager? = null
    }
}