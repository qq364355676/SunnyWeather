package com.lcx.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lcx.sunnyweather.logic.Repository
import com.lcx.sunnyweather.logic.model.Location

/**
 *@author lcx
 *@date 2021/1/27
 *@desc weatherViewModel
 */
class WeatherViewModel: ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var lng = ""
    var lat = ""
    var placeName = ""
    val weatherLiveData = Transformations.switchMap(locationLiveData){location ->
        Repository.refreshWeather(location.lng, location.lat)
    }
    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}