package br.com.koruthos.rickmorty.models

import com.google.gson.annotations.SerializedName

data class Personagem(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var nome: String,
    @SerializedName("status") var status: String,
    @SerializedName("species") var especie: String,
    @SerializedName("type") var tipo: String,
    @SerializedName("gender") var genero: String,
    @SerializedName("image") var urlImagem: String,
)
