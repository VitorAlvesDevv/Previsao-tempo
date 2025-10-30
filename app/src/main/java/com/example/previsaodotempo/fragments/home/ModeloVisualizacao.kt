package com.example.previsaodotempo.fragments.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.data.CurrentPrevisao
import com.example.previsaodotempo.data.EventDataLive
import com.example.previsaodotempo.rede.repositorio.RepositorioPrevisaoTempo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch

class ModeloVisualizacao(private val repositorioPrevisaoTempo: RepositorioPrevisaoTempo) : ViewModel() {

    //Região Current Location
    private val _currentLocation = MutableLiveData<EventDataLive<CurrentLocationDataState>>()
    val currentLocation: LiveData<EventDataLive<CurrentLocationDataState>> get() = _currentLocation

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
            runCatching {
                repositorioPrevisaoTempo.updateAddressText(currentLocation, geocoder)
            }.onSuccess { location ->
                emitCurrentLocationUiState(currentLocation = location)
            }.onFailure {
                emitCurrentLocationUiState(
                    currentLocation = currentLocation.copy(
                        location = "N/A"
                    )
                )
            }


        }
    }

    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(isLoading, currentLocation, error)
        _currentLocation.value = EventDataLive(currentLocationDataState)
    }

    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )
// Acaba Regiao

    private val _previsaoData = MutableLiveData<EventDataLive<PrevisaoDataState>>()
    val previsaoData: LiveData<EventDataLive<PrevisaoDataState>> get() = _previsaoData

    fun getPrevisaoData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            emitPrevisaoDataUiState(isLoading = true)
            repositorioPrevisaoTempo.getPrevisaoData(latitude, longitude)?.let { previsaoData ->
                emitPrevisaoDataUiState(
                    currentPrevisao = CurrentPrevisao(
                        icon = previsaoData.current.condition.icon,
                        temperature = previsaoData.current.temperature,
                        wind = previsaoData.current.wind,
                        humidity = previsaoData.current.humidity,
                        chanceOfRain = previsaoData.forecast.forecastDia.first().day.chanceOfRain


                    )
                )
            } ?: emitPrevisaoDataUiState(error = "Não foi possível obter os dados meteorológicos")
        }
    }

    private fun emitPrevisaoDataUiState(
        isLoading: Boolean = false,
        currentPrevisao: CurrentPrevisao? = null,
        error: String? = null
    )
    {
        val previsaoDataState = PrevisaoDataState(isLoading, currentPrevisao, error)
        _previsaoData.value = EventDataLive(previsaoDataState)
    }



    data class PrevisaoDataState(
        val isLoading: Boolean,
        val currentPrevisao: CurrentPrevisao?,
        val error: String?
    )
}