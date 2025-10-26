package com.example.previsaodotempo.injecaodependencias

import com.example.previsaodotempo.rede.repositorio.RepositorioPrevisaoTempo
import org.koin.dsl.module

val moduloRepositorio = module {
    single { RepositorioPrevisaoTempo() }
}