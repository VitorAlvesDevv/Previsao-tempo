package com.example.previsaodotempo.utis
import android.app.Application
import com.example.previsaodotempo.injecaodependencias.modeloVisualizacao
import com.example.previsaodotempo.injecaodependencias.moduleArmazem
import com.example.previsaodotempo.injecaodependencias.moduloRede
import com.example.previsaodotempo.injecaodependencias.moduloRepositorio
import com.example.previsaodotempo.injecaodependencias.moduloSerilizacao
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ConfiguracaodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ConfiguracaodoApp)
            modules(
                listOf(
                    moduloRepositorio,
                    modeloVisualizacao,
                    moduloSerilizacao,
                    moduleArmazem,
                    moduloRede
                    ))
        }
    }
}