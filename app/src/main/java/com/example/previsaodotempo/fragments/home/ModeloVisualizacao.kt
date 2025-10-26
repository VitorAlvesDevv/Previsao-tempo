package com.example.previsaodotempo.fragments.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.rede.repositorio.RepositorioPrevisaoTempo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch

class ModeloVisualizacao(private val repositorioPrevisaoTempo: RepositorioPrevisaoTempo) : ViewModel() {

    private val _currentLocation = MutableLiveData<CurrentLocationDataState>()
    val currentLocation: LiveData<CurrentLocationDataState> get() = _currentLocation

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUiState(isLoading = true)
            repositorioPrevisaoTempo.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                    updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUiState(error = "não foi possivel buscar a localização atual")
                }
            )
        }
    }

    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder) {
        viewModelScope.launch {
            val location = repositorioPrevisaoTempo.updateAddressText(currentLocation, geocoder)
            emitCurrentLocationUiState(currentLocation = location)
        }
    }

    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(isLoading, currentLocation, error)
        _currentLocation.value = currentLocationDataState
    }

    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )

}