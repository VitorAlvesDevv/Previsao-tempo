package com.example.previsaodotempo.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class PrevisaoData

data class CurrentLocation(
    val date: String = getCurrentDate(),
    val location: String = "Escolha sua Localização",
    val latitude: Double? = null,
    val longitude: Double? = null

) : PrevisaoData()

data class CurrentPrevisao(
    val icon: String,
    val temperature: Float,
    val wind: Float,
    val humidity: Int,
    val chanceOfRain: Int
) : PrevisaoData

private fun getCurrentDate(): String {
    val currentDate = Date()
    val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    return "Hoje, ${formatter.format(currentDate)}"
}