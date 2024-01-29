package com.example.calldefender.service

import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.UPDATE_CALLS_ACTION
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class CallDefenderPhoneStateListener(
    private val context: Context,
) : PhoneStateListener() {

    private var isAcceptedCall = false

    @Inject
    lateinit var repository: CallsRepository

    init {
        (context.applicationContext as CallDefenderApp).appComponent.inject(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onCallStateChanged(state: Int, phoneNumber: String) {
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                return
            }

            TelephonyManager.CALL_STATE_OFFHOOK -> {
                addCallToRepository(phoneNumber, CallType.ACCEPTED)
                isAcceptedCall = true
            }

            TelephonyManager.CALL_STATE_IDLE -> {
                if (!isAcceptedCall)
                    addCallToRepository(phoneNumber, CallType.REJECTED)
                isAcceptedCall = false
            }
        }
    }

    private fun addCallToRepository(callNumber: String, callType: CallType) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addCall(
                CallUi(
                    callNumber,
                    Date().formatToPattern(DatePatterns.DEFAULT.pattern),
                    callType
                )
            )
            updateUI(context)
        }
    }

    private fun updateUI(context: Context) {
        val intent = Intent(UPDATE_CALLS_ACTION)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}