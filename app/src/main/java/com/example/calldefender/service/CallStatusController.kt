package com.example.calldefender.service

interface CallStatusController {
    suspend fun checkPhone(phoneNumber: String): Boolean
}