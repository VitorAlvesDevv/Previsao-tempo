package com.example.previsaodotempo.rede.repositorio

import android.annotation.SuppressLint
import android.location.Geocoder
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.data.LocalizacaoRemota
import com.example.previsaodotempo.data.RemotePrevisaoData
import com.example.previsaodotempo.rede.api.PrevisaoAPI
import com.google.android.gms.location.Priority
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource





class RepositorioPrevisaoTempo(private val previsaoAPI: PrevisaoAPI) {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onSuccess: (currentLocation: CurrentLocation) -> Unit,
        onFailure: () -> Unit
    ) {
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            location ?: onFailure()
            onSuccess(
                CurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )

        }.addOnFailureListener { onFailure() }
    }
    @Suppress ("DEPRECATION")
    fun updateAddressText(
        currentLocation: CurrentLocation,
        geocoder: Geocoder
    ): CurrentLocation{
        val latitude = currentLocation.latitude ?: return currentLocation
        val longitude = currentLocation.longitude ?: return currentLocation
        return geocoder.getFromLocation(latitude, longitude, 1)?.let { addresses ->
            val address = addresses[0]
            val addressText = StringBuilder()
            addressText.append(address.locality).append(", ")
            addressText.append(address.adminArea).append(", ")
            addressText.append(address.countryName)
            currentLocation.copy(
                location = addressText.toString()
            )
        } ?: currentLocation
    }
    
    suspend fun  searchLocation(query: String): List<LocalizacaoRemota>? {
        val response = previsaoAPI.searchLocation(query = query)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun  getPrevisaoData(latitude: Double, longitude: Double): RemotePrevisaoData? {
        val response = previsaoAPI.getPrevisaoData(query = "$latitude,$longitude")
        return if (response.isSuccessful) response.body() else null
    }

}