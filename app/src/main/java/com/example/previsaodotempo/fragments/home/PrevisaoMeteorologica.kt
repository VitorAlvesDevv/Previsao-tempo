package com.example.previsaodotempo.fragments.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.data.PrevisaoData
import com.example.previsaodotempo.databinding.ItemContainerCurrentLocationBinding

class PrevisaoMeteorologica(
    private val onLocationClicked: () -> Unit
) : RecyclerView.Adapter<PrevisaoMeteorologica.CurrentLocationViewHolder>(){

    private val PrevisaoData = mutableListOf<PrevisaoData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<PrevisaoData>) {
        PrevisaoData.clear()
        PrevisaoData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentLocationViewHolder {
        return CurrentLocationViewHolder(
            ItemContainerCurrentLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun  onBindViewHolder(holder: CurrentLocationViewHolder, position: Int) {
        holder.bind(PrevisaoData[position] as CurrentLocation)
    }

    override fun getItemCount(): Int {
        return PrevisaoData.size
    }




    inner class CurrentLocationViewHolder(
        private val binding: ItemContainerCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
            with(binding) {
                textCurrentDate.text = currentLocation.date
                textCurrentLocation.text = currentLocation.location
                imageCurrentLocation.setOnClickListener {onLocationClicked() }
                textCurrentLocation.setOnClickListener {onLocationClicked()}
            }
        }
    }
}