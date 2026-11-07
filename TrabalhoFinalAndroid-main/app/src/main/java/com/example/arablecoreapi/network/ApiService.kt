package com.example.arablecoreapi.network

import com.example.arablecoreapi.data.remote.BuscaResposta
import com.example.arablecoreapi.data.remote.Especie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Só plantas (highertaxonKey=6), espécies aceitas, sem repetidos.
    @GET("species/search?rank=SPECIES&highertaxonKey=6&status=ACCEPTED&datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&limit=20")
    suspend fun buscar(@Query("q") texto: String): BuscaResposta

    @GET("species/{key}")
    suspend fun detalhe(@Path("key") key: Int): Especie
}
