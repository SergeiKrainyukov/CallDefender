package com.example.calldefender.repository

import com.example.calldefender.ui.model.CallUi

interface CallsRepository {
    suspend fun getCalls(): List<CallUi>
    suspend fun addCall(callUi: CallUi)
}