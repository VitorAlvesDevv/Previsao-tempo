package com.example.previsaodotempo.rede.api

import com.example.previsaodotempo.data.LocalizacaoRemota
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PrevisaoAPI {
    companion object {
        const val  BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "e045d4338c544965a0c211506252610"
    }

    @GET("search.json")
    suspend fun searchLocation (
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<List<LocalizacaoRemota>>
}