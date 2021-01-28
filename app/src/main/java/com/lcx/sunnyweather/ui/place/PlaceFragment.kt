package com.lcx.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lcx.sunnyweather.R
import com.lcx.sunnyweather.bindView
import com.lcx.sunnyweather.databinding.FragmentPlaceBinding
import com.lcx.sunnyweather.ui.weather.WeatherActivity
import com.lcx.sunnyweather.util.showToast

/**
 *@author lcx
 *@date 2021/1/27
 *@desc PlaceFragment
 */
class PlaceFragment: Fragment(R.layout.fragment_place) {

    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }
    private val binding: FragmentPlaceBinding by bindView()
    private lateinit var adapter: PlaceAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavePlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener { editable: Editable? ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlace(content)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                "未能查询到任何地点".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

}