package com.example.calldefender.service

import android.content.Intent
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
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class CallDefenderCallScreeningService : CallScreeningService() {

    @Inject
    lateinit var callsRepository: CallsRepository

    @Inject
    lateinit var callStatusController: CallStatusController

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        (application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onScreenCall(callDetails: Details) {
        val phoneNumber = callDetails.handle.toString().removeTelPrefix().parseCountryCode()
        val response = CallResponse.Builder()
        CoroutineScope(Dispatchers.IO).launch {
            if (callStatusController.checkPhone(phoneNumber)) respondToCall(
                callDetails,
                response.build()
            ) else rejectCall(callDetails, response, phoneNumber)
            updateUI()
        }
    }

    private fun addCallToDb(callNumber: String, callType: CallType) {
        CoroutineScope(Dispatchers.IO).launch {
            callsRepository.addCall(
                CallUi(
                    callNumber,
                    Date().formatToPattern(DatePatterns.DEFAULT.pattern),
                    callType
                )
            )
            updateUI()
        }
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
        addCallToDb(phoneNumber, CallType.REJECTED)
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