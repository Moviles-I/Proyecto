package com.example.myapplication.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    // llaves para pasar datos entre pantallas con Intent
    const val EXTRA_REPORT_ID = "report_id"
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"

    // ubicacion por defecto del mapa (Orotina, aproximada)
    const val DEFAULT_LATITUDE = 9.9114
    const val DEFAULT_LONGITUDE = -84.5236

    // fecha de hoy con el formato de las tarjetas, ej. 12/08/26
    fun getCurrentDate(): String {
        val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return format.format(Date())
    }
}