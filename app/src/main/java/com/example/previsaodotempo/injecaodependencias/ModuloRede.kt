package com.example.previsaodotempo.injecaodependencias
import com.example.previsaodotempo.rede.api.PrevisaoAPI
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val moduloRede = module {
    factory {okHttpClient()}
    single {retrofit(okHttpClient = get())}
    factory {previsaoAPI(retrofit = get())}
}

private fun okHttpClient() = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .retryOnConnectionFailure(false)
    .build()

private fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(PrevisaoAPI.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun previsaoAPI(retrofit: Retrofit) = retrofit.create(PrevisaoAPI::class.java)
