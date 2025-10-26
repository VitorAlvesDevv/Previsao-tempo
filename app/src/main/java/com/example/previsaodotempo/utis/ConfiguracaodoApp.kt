package com.example.previsaodotempo.utis
import android.app.Application
import com.example.previsaodotempo.injecaodependencias.modeloVisualizacao
import com.example.previsaodotempo.injecaodependencias.moduloRepositorio
import org.koin.core.context.startKoin

class ConfiguracaodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(moduloRepositorio, modeloVisualizacao))
        }
    }
}