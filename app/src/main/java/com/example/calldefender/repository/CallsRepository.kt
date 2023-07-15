package com.example.calldefender.repository

import com.example.calldefender.ui.model.CallUi
import io.reactivex.Completable
import io.reactivex.Single

interface CallsRepository {
    fun getCalls(): Single<List<CallUi>>
    fun addCall(callUi: CallUi): Completable
}