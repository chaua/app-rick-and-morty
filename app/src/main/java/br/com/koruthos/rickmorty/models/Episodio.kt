package br.com.koruthos.rickmorty.models

import com.google.gson.annotations.SerializedName

data class Episodio(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var nome: String,
    @SerializedName("air_date") var dataExibicao: String,
    @SerializedName("episode") var numero: String,
    @SerializedName("url") var url: String,
)
