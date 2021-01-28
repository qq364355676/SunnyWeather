package com.lcx.sunnyweather.ui.place

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lcx.sunnyweather.BindingViewHolder
import com.lcx.sunnyweather.databinding.PlaceItemBinding
import com.lcx.sunnyweather.logic.model.Place
import com.lcx.sunnyweather.newBindingViewHolder
import com.lcx.sunnyweather.ui.weather.WeatherActivity

/**
 *@author lcx
 *@date 2021/1/27
 *@desc PlaceAdapter
 */
class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>): RecyclerView.Adapter<BindingViewHolder<PlaceItemBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<PlaceItemBinding>{
        val holder = newBindingViewHolder<PlaceItemBinding>(parent)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder<PlaceItemBinding>, position: Int) {
        val place = placeList[position]
        holder.binding.placeName.text = place.name
        holder.binding.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}

