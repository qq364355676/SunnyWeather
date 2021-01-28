package com.lcx.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.lcx.sunnyweather.MyApplication
import com.lcx.sunnyweather.logic.model.Place

/**
 *@author lcx
 *@date 2021/1/28
 *@desc PlaceDao
 */
object PlaceDao {

    fun savePlace(place: Place) {
        getSharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavePlace() : Place{
        val placeJson = getSharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = getSharedPreferences().contains("place")

    private fun getSharedPreferences() = MyApplication.context.
            getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
}