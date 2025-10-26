package com.example.previsaodotempo.injecaodependencias

import com.google.gson.Gson
import org.koin.dsl.module

val moduloSerilizacao = module {
    single { Gson() }
}