package com.example.calldefender.ui.model

import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.data.CallEntity
import java.util.Date

data class CallUi(val callNumber: String, val callDate: String, val callType: CallType) {
    companion object {
        fun fromEntity(
            callEntity: CallEntity
        ) = CallUi(
            callEntity.callNumber,
            Date(callEntity.callDate).formatToPattern(DatePatterns.DEFAULT.pattern),
            CallType.values()[callEntity.callType]
        )
    }
}