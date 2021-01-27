package com.lcx.sunnyweather.logic

import androidx.lifecycle.liveData
import com.lcx.sunnyweather.logic.model.Place
import com.lcx.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

/**
 *@author lcx
 *@date 2021/1/26
 *@desc Repository 仓库层用于判断数据应该是从本地获取还是从网络获取
 */
object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                Result.success(placeResponse.places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}