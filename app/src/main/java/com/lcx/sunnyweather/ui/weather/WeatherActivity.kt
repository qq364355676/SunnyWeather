package com.lcx.sunnyweather.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcx.sunnyweather.R
import com.lcx.sunnyweather.databinding.ActivityWeatherBinding
import com.lcx.sunnyweather.inflate
import com.lcx.sunnyweather.logic.model.Weather
import com.lcx.sunnyweather.logic.model.getSky
import com.lcx.sunnyweather.util.showToast
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    private val binding: ActivityWeatherBinding by inflate()
    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置沉浸式状态栏
        //在对应的xml布局中设置fitsSystemWindows = true 这里在now.xml中设置
        //表示会为系统状态栏留出空间
        val decorView = window.decorView
        // 通过decorView.setSystemUiVisibility() 改变系统UI的显示
        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 和 View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // 表示activity的布局会显示在状态栏上面
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // 最后将状态栏设置成透明
        window.statusBarColor = Color.TRANSPARENT
        if (viewModel.lng.isEmpty()) {
            viewModel.lng = intent.getStringExtra("location_lng") ?: ""
        }
        Log.e("TAG","经度：${viewModel.lng}")
        if (viewModel.lat.isEmpty()) {
            viewModel.lat = intent.getStringExtra("location_lat") ?: ""
        }
        Log.e("TAG","纬度：${viewModel.lat}")
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
//            Log.e("TAG", "天气：${weather.toString()}")
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                "无法成功获取天气信息".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.lng, viewModel.lat)
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.nowLayoutInclude.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充实况天气数据，now.xml
        binding.nowLayoutInclude.currentTemp.text = "${realtime.temperature.toInt()}℃"
        binding.nowLayoutInclude.currentSky.text = getSky(realtime.skycon).info
        binding.nowLayoutInclude.currentAqi.text = "空气指数:${realtime.airQuality.aqi.chn.toInt()}"
        binding.nowLayoutInclude.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // 填充预报天气，forecast.xml
        binding.forecastLayoutInclude.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temp = daily.temperature[i]
            val view = LayoutInflater.from(this)
                    .inflate(R.layout.forecast_item,binding.forecastLayoutInclude.forecastLayout,false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val tempInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.bg)
            skyInfo.text = sky.info
            tempInfo.text = "${temp.min.toInt()} ~ ${temp.max.toInt()}"
            binding.forecastLayoutInclude.forecastLayout.addView(view)
        }
        // 填充生活指数，life_index.xml
        val lifeIndex = daily.lifeIndex
        binding.lifeIndexLayoutInclude.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndexLayoutInclude.dressText.text = lifeIndex.dressing[0].desc
        binding.lifeIndexLayoutInclude.carWashText.text = lifeIndex.carWashing[0].desc
        binding.lifeIndexLayoutInclude.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }
}