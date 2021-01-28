package com.lcx.sunnyweather.logic.network

import com.lcx.sunnyweather.MyApplication
import com.lcx.sunnyweather.logic.model.DailyResponse
import com.lcx.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *@author lcx
 *@date 2021/1/27
 *@desc WeatherService
 */
interface WeatherService {
    @GET("v2.5/${MyApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng: String, @Path("lat")lat: String): Call<RealtimeResponse>

    @GET("v2.5/${MyApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng")lng: String, @Path("lat")lat: String): Call<DailyResponse>
}