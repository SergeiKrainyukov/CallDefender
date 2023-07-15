package com.example.calldefender.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "callNumber") val callNumber: String,
    @ColumnInfo(name = "date") val callDate: Long,
    @ColumnInfo(name = "callType") val callType: Int
)