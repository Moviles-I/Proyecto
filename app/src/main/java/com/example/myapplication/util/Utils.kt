package com.example.myapplication.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    // Keys for passing data between screens using Intent
    const val EXTRA_REPORT_ID = "report_id"
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"

    // Default map location (Orotina, approximate)
    const val DEFAULT_LATITUDE = 9.9114
    const val DEFAULT_LONGITUDE = -84.5236

    // Current date formatted for cards, e.g., 12/08/26
    fun getCurrentDate(): String {
        val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return format.format(Date())
    }
}
