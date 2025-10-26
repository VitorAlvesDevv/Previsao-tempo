package com.example.previsaodotempo.injecaodependencias

import com.example.previsaodotempo.fragments.home.ModeloVisualizacao
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modeloVisualizacao = module {
    viewModel{ ModeloVisualizacao(repositorioPrevisaoTempo = get()) }
}