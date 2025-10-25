package com.example.previsaodotempo.data

sealed class PrevisaoData

data class CurrentLocation(
    val date: String,
    val location: String = "Escolha sua Localização"
) : PrevisaoData()