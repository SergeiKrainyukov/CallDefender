package com.example.calldefender.service

import io.reactivex.Observable

interface CallStatusController {
    fun checkPhone(phoneNumber: String): Observable<Boolean>
}