package com.lcx.sunnyweather.ui.place

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lcx.sunnyweather.BindingViewHolder
import com.lcx.sunnyweather.databinding.PlaceItemBinding
import com.lcx.sunnyweather.logic.model.Place
import com.lcx.sunnyweather.newBindingViewHolder

/**
 *@author lcx
 *@date 2021/1/27
 *@desc PlaceAdapter
 */
class PlaceAdapter(private val placeList: List<Place>): RecyclerView.Adapter<BindingViewHolder<PlaceItemBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = newBindingViewHolder<PlaceItemBinding>(parent)

    override fun onBindViewHolder(holder: BindingViewHolder<PlaceItemBinding>, position: Int) {
        val place = placeList[position]
        holder.binding.placeName.text = place.name
        holder.binding.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}