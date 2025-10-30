package com.example.previsaodotempo.fragments.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.data.PrevisaoData
import com.example.previsaodotempo.databinding.ItemContainerCurrentLocationBinding

class PrevisaoMeteorologica(
    private val onLocationClicked: () -> Unit
) : RecyclerView.Adapter<PrevisaoMeteorologica.CurrentLocationViewHolder>(){

    private companion object {
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_PREVISAO = 0
        const val INDEX_FORECAST = 2

    }

    private val previsaoData = mutableListOf<PrevisaoData>()



    fun setCurrentLocation(currentLocation: CurrentLocation) {
        if (previsaoData.isEmpty()) {
            previsaoData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(INDEX_CURRENT_LOCATION)
        } else {
            previsaoData[INDEX_CURRENT_PREVISAO] = currentLocation
            notifyItemChanged(INDEX_CURRENT_PREVISAO)
        }
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
        holder.bind(previsaoData[position] as CurrentLocation)
    }

    override fun getItemCount(): Int {
        return previsaoData.size
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