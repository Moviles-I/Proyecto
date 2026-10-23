package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.util.Utils

// Pantalla 3: ubicacion que el usuario elige en el mapa
class LocationViewModel : ViewModel() {

    var latitude: Double = Utils.DEFAULT_LATITUDE
    var longitude: Double = Utils.DEFAULT_LONGITUDE

    fun setLocation(lat: Double, lon: Double) {
        latitude = lat
        longitude = lon
    }
}