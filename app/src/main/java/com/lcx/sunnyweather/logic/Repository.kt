package com.lcx.sunnyweather.logic

import androidx.lifecycle.liveData
import com.lcx.sunnyweather.logic.dao.PlaceDao
import com.lcx.sunnyweather.logic.model.Place
import com.lcx.sunnyweather.logic.model.Weather
import com.lcx.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 *@author lcx
 *@date 2021/1/26
 *@desc Repository 仓库层用于判断数据应该是从本地获取还是从网络获取
 */
object Repository {

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavePlace() = PlaceDao.getSavePlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            Result.success(placeResponse.places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 同时获取实况天气和预报天气数据，
     * 使用async函数来获取数据，并调用await()方法来确保两个网络请求都响应成功后才会进行下一步操作
     * 另因为async函数必须在协程作用域中才能调用，通过coroutineScope函数创建一个协程作用域
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO){
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException(
                    "realtime response status is ${realtimeResponse.status}" +
                            " daily response status is ${dailyResponse.status}"
                ))
            }
        }
    }

    /**
     * 网络访问有可能会发生异常，所以对入口函数进一步封装，只需进行一次try-catch
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context){
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}