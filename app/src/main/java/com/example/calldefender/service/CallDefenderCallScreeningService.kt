package com.example.calldefender.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.Call.Details
import android.telecom.CallScreeningService
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.UPDATE_CALLS_ACTION
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.common.parseCountryCode
import com.example.calldefender.common.removeTelPrefix
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.ui.MainActivity
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
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
            ) else {
                rejectCall(callDetails, response, phoneNumber)
                sendNotification(phoneNumber)
            }
            updateUI()
        }
    }

    private fun sendNotification(phoneNumber: String) {

        val channelId = "BLOCKED_CALL_NOTIFICATION_CHANNEL"
        val channelName = "Blocked Call Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Входящий звонок заблокирован")
            .setContentText(phoneNumber)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_call)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: запросить разрешение
        }
        else
            NotificationManagerCompat.from(this).notify(0, builder)

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
}