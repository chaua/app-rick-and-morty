package br.com.koruthos.cursoandroid.networks

import br.com.koruthos.rickmorty.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Classe para gerenciar as chamadas aos serviços da API.
 */
object NetworkManager {

    // URL base da API
    private val URL = "https://rickandmortyapi.com/api/"

    lateinit var client: OkHttpClient

    val service: ApiService by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return@lazy retrofit.create(ApiService::class.java)
    }

    fun stop() {
        client.dispatcher().cancelAll()
    }
}
