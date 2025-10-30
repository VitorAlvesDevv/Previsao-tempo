package com.example.previsaodotempo.fragments.home

import coil.load
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.data.CurrentPrevisao
import com.example.previsaodotempo.data.PrevisaoData
import com.example.previsaodotempo.databinding.ItemContainerCurrentLocationBinding
import com.example.previsaodotempo.databinding.ItemContainerCurrentPrevisaoBinding

class PrevisaoMeteorologica(
    private val onLocationClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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

    fun setCurrentPrevisao(currentPrevisao: CurrentPrevisao) {
        if (previsaoData.getOrNull(INDEX_CURRENT_PREVISAO) != null) {
            previsaoData[INDEX_CURRENT_PREVISAO] = currentPrevisao
            notifyItemChanged(INDEX_CURRENT_PREVISAO)
        }else {
            previsaoData.add(INDEX_CURRENT_PREVISAO,currentPrevisao)
            notifyItemInserted(INDEX_CURRENT_PREVISAO)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemContainerCurrentLocationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

         else -> CurrentPrevisaoViewHolder(
             ItemContainerCurrentPrevisaoBinding.inflate(
                 LayoutInflater.from(parent.context),
                 parent,
                 false
             )
         )

        }
    }

    override fun  onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CurrentLocationViewHolder -> holder.bind(previsaoData[position] as CurrentLocation)
            is CurrentPrevisaoViewHolder -> holder.bind(previsaoData[position] as CurrentPrevisao)
        }
    }

    override fun getItemCount(): Int {
        return previsaoData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(previsaoData[position]) {
           is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentPrevisao -> INDEX_CURRENT_PREVISAO
        }
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

    inner class CurrentPrevisaoViewHolder(
        private val binding: ItemContainerCurrentPrevisaoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentPrevisao: CurrentPrevisao) {
            with(binding) {
                imageIcon.load("https:${currentPrevisao.icon}"){ crossfade(true) }
                textTemperature.text = String.format("%s\u00B0C", currentPrevisao.temperature)
                textWind.text  = String.format("%s km/h", currentPrevisao.wind)
                textHumidade.text = String.format("%s%%", currentPrevisao.chanceOfRain)
            }
        }
    }
}