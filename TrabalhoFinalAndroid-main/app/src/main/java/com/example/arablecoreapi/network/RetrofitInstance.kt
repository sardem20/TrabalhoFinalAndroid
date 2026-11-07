package com.example.arablecoreapi.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private val json = Json { ignoreUnknownKeys = true }

    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://api.gbif.org/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)
}
