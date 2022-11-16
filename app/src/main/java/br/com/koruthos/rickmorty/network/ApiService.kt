package br.com.koruthos.rickmorty.network

import br.com.koruthos.rickmorty.models.Episodio
import br.com.koruthos.rickmorty.models.Personagem
import br.com.koruthos.rickmorty.models.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface que mapeia todos os endpoints da API
 */
interface ApiService {

    @GET("character")
    fun listarPersonagens(@Query("page") pagina: Int): Call<Response<Personagem>>

    @GET("character")
    fun listarEpisodios(@Query("page") pagina: Int): Call<Response<Episodio>>
}
