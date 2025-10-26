package com.example.previsaodotempo.injecaodependencias

import com.example.previsaodotempo.armazenar.PreferenciasCompartilhadas
import org.koin.dsl.module

val moduleArmazem = module {
    single { PreferenciasCompartilhadas(context = get(), gson= get()) }
}