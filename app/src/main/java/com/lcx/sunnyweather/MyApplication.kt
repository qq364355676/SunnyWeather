package com.lcx.sunnyweather

import android.app.Application
import android.content.Context

/**
 *@author lcx
 *@date 2021/1/26
 *@desc MyApplication
 */
class MyApplication : Application() {
    companion object{
        lateinit var context: Context
        const val TOKEN = "gAncELjwWmE19Dxj"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}