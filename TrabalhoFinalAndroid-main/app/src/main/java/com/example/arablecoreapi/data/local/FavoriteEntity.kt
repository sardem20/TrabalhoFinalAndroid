package com.example.arablecoreapi.data.local

import androidx.room.Entity

// A chave é (userId + especieKey): cada usuário tem os seus favoritos.
@Entity(tableName = "favoritos", primaryKeys = ["userId", "especieKey"])
data class FavoriteEntity(
    val userId: Long,
    val especieKey: Int,
    val nome: String,
    val familia: String?
)
