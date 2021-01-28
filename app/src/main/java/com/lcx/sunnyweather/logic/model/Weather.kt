package com.lcx.sunnyweather.logic.model

/**
 *@author lcx
 *@date 2021/1/27
 *@desc 天气类，包含实况天气和预报天气
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
