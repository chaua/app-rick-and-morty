package br.com.koruthos.rickmorty.models

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val resultados: ArrayList<T>
)
