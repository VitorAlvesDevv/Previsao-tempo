package com.example.previsaodotempo.data

import com.google.gson.annotations.SerializedName

data class RemotePrevisaoData(
    val current: CurrentPrevisaoRemota,
    val forecast: ForecastRemoto


)

data class CurrentPrevisaoRemota(
    @SerializedName("temp_c") val temperature: Float,
    val condition: PrevisaoConditionRemota,
    @SerializedName("wind_kph") val wind: Float,
    val humidity: Int
)

data class  ForecastRemoto(
    @SerializedName("forecastday") val forecastDia: List<ForecastDiaRemote>
)

data class  ForecastDiaRemote(
    val day: DiaRemoto,
    val hour: List<ForecastHoraRemote>
)

data class DiaRemoto(
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int
)

data class ForecastHoraRemote (
    val time: String,
    @SerializedName("temp_c") val temperature: Float,
    @SerializedName("feelslike_c") val feelsLikeTemperature: Float,
    val condition: PrevisaoConditionRemota
)

data class PrevisaoConditionRemota(
    val icon: String
)
