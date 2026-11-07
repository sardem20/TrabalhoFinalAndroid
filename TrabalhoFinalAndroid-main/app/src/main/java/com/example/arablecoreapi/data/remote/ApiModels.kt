package com.example.arablecoreapi.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuscaResposta(val results: List<Especie> = emptyList())

@Serializable
data class Especie(
    val key: Int,
    val scientificName: String = "",
    val canonicalName: String? = null,
    val kingdom: String? = null,
    val phylum: String? = null,
    @SerialName("class") val classe: String? = null, // "class" é palavra reservada no Kotlin
    val order: String? = null,
    val family: String? = null,
    val genus: String? = null
) {
    val nome: String get() = canonicalName ?: scientificName
}
