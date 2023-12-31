package com.example.calldefender.common

import android.net.Uri

/**
 * Removes 'tel:' prefix from phone number string.
 */
fun String.removeTelPrefix() = this.replace(TEL_PREFIX, "")

/**
 * Phone call numbers can contain prefix of country like '+385' and '+' sign will be interpreted
 * like '%2B', so this must be decoded.
 */
fun String.parseCountryCode(): String = Uri.decode(this)