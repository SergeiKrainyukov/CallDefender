package com.example.calldefender.repository

import com.example.calldefender.ui.model.CallUi
import io.reactivex.Completable

interface CallsRepository {
    suspend fun getCalls(): List<CallUi>
    fun addCall(callUi: CallUi): Completable
}