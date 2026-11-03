package com.example.arablecoreapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favoritos WHERE userId = :userId ORDER BY nome")
    suspend fun listar(userId: Long): List<FavoriteEntity>

    @Query("SELECT COUNT(*) FROM favoritos WHERE userId = :userId AND especieKey = :key")
    suspend fun contar(userId: Long, key: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: FavoriteEntity)

    @Query("DELETE FROM favoritos WHERE userId = :userId AND especieKey = :key")
    suspend fun remover(userId: Long, key: Int)
}
