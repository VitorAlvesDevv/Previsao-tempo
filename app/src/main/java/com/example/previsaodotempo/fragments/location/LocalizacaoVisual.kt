package com.example.previsaodotempo.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.previsaodotempo.data.LocalizacaoRemota
import com.example.previsaodotempo.rede.repositorio.RepositorioPrevisaoTempo

class LocalizacaoVisual (private val  repositorioPrevisaoTempo: RepositorioPrevisaoTempo): ViewModel() {

    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> get() = _searchResult

    fun searchLocation(query: String) {
        emitSearchResultUiState(isLoading = true)
        val searchResult = repositorioPrevisaoTempo.searchLocation(query)
        if (searchResult.isNullOrEmpty()) {
            emitSearchResultUiState(error = "Localização nao encontrada")
        } else {
            emitSearchResultUiState(locations = searchResult)
        }
    }

    private fun emitSearchResultUiState(
        isLoading: Boolean = false,
        locations: List<LocalizacaoRemota>? = null,
        error: String? = null
    ) {
        val searchResultDataState = SearchResultDataState(isLoading, locations, error)
        _searchResult.value = searchResultDataState
    }


    data class SearchResultDataState(
        val isLoading: Boolean,
        val locations: List<LocalizacaoRemota>?,
        val error: String?
    )
}