package com.example.previsaodotempo.injecaodependencias

import com.example.previsaodotempo.fragments.home.ModeloVisualizacao
import com.example.previsaodotempo.fragments.location.LocalizacaoVisual
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modeloVisualizacao = module {
    viewModel{ ModeloVisualizacao(repositorioPrevisaoTempo = get()) }
    viewModel{ LocalizacaoVisual(repositorioPrevisaoTempo = get())}
}