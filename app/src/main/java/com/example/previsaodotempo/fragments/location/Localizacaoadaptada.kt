package com.example.previsaodotempo.fragments.location;

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.data.LocalizacaoRemota
import com.example.previsaodotempo.databinding.ItemContainerLocationBinding

import kotlin.Unit;

class LocalizacaoAdaptada (
    private val onLocationClicked: (LocalizacaoRemota) -> Unit
) : RecyclerView.Adapter<LocalizacaoAdaptada.LocationViewHolder>(){

    private val locations = mutableListOf<LocalizacaoRemota>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<LocalizacaoRemota>) {
        locations.clear()
        locations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            ItemContainerLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(localizacaoRemota = locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class LocationViewHolder(
        private val binding: ItemContainerLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind (localizacaoRemota: LocalizacaoRemota) {
            with(localizacaoRemota) {
                val location = "$name, $region, $country"
                binding.textRemoteLocation.text = location
                binding.root.setOnClickListener { onLocationClicked(localizacaoRemota) }
            }
        }
    }
}