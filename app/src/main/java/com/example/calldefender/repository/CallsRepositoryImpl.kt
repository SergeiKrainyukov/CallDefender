package com.example.calldefender.repository

import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.common.stringToDate
import com.example.calldefender.data.CallEntity
import com.example.calldefender.data.CallEntityDao
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class CallsRepositoryImpl @Inject constructor(
    private val callEntityDao: CallEntityDao
) : CallsRepository {
    override fun getCalls(): Single<List<CallUi>> {
        return callEntityDao.getAll().map {
            it.map {
                CallUi(
                    it.callNumber,
                    Date(it.callDate).formatToPattern(DatePatterns.DEFAULT.pattern),
                    CallType.values()[it.callType]
                )
            }
        }
    }

    override fun addCall(callUi: CallUi): Completable {
        return callEntityDao.insert(
            CallEntity(
                callNumber = callUi.callNumber,
                callDate = stringToDate(callUi.callDate)!!.time,
                callType = callUi.callType.ordinal
            )
        )
    }
}