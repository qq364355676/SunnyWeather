package com.lcx.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lcx.sunnyweather.logic.Repository
import com.lcx.sunnyweather.logic.model.Place

/**
 *@author lcx
 *@date 2021/1/26
 *@desc PlaceViewModel
 */
class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlace(query: String) {
        searchLiveData.value = query
    }
}