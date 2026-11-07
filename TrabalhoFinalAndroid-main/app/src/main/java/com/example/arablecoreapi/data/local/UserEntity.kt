package com.example.arablecoreapi.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val email: String,
    val senhaHash: String,
    val tipo: String = Tipo.FAZENDEIRO
)

// Os três tipos de usuário do app.
object Tipo {
    const val FAZENDEIRO = "FAZENDEIRO"
    const val AGRONOMO = "AGRONOMO"
    const val BOTANICO = "BOTANICO"

    fun nome(tipo: String) = when (tipo) {
        AGRONOMO -> "Agrônomo"
        BOTANICO -> "Botânico"
        else -> "Fazendeiro"
    }
}
