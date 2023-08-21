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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
        disposables.add(
            callStatusController.checkPhone(phoneNumber).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if (it) {
                        respondToCall(callDetails, response.build())
                    } else {
                        rejectCall(callDetails, response, phoneNumber)
                    }
                    updateUI()
                }
        )
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