package com.example.calldefender.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatToPattern(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(
        time
    )

fun stringToDate(dateString: String): Date? {
    val format = SimpleDateFormat(DatePatterns.DEFAULT.pattern, Locale.getDefault())
    return try {
        format.parse(dateString)
    } catch (e: Exception) {
        null
    }
}


enum class DatePatterns(val pattern: String) {
    DEFAULT("dd.MM.yyyy hh:mm:ss")
}