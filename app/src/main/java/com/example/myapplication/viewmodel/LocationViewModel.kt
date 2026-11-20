package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.util.Utils

// Screen 3: location chosen by the user on the map
class LocationViewModel : ViewModel() {

    var latitude: Double = Utils.DEFAULT_LATITUDE
    var longitude: Double = Utils.DEFAULT_LONGITUDE

    fun setLocation(lat: Double, lon: Double) {
        latitude = lat
        longitude = lon
    }
}
