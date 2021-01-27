package com.lcx.sunnyweather.logic.network

import com.lcx.sunnyweather.MyApplication
import com.lcx.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *@author lcx
 *@date 2021/1/26
 *@desc PlaceService
 */
interface PlaceService {

    @GET("v2/place?token=${MyApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}