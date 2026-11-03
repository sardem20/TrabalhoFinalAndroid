package com.example.arablecoreapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun inserir(usuario: UserEntity): Long

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): UserEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): UserEntity?
}
