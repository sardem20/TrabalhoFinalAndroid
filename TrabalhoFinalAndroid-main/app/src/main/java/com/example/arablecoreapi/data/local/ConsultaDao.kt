package com.example.arablecoreapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConsultaDao {
    @Insert
    suspend fun inserir(consulta: ConsultaEntity)

    @Query("SELECT * FROM consultas WHERE fazendeiroId = :id ORDER BY id DESC")
    suspend fun doFazendeiro(id: Long): List<ConsultaEntity>

    @Query("SELECT * FROM consultas WHERE especialistaId = :id ORDER BY id DESC")
    suspend fun doEspecialista(id: Long): List<ConsultaEntity>

    @Query("UPDATE consultas SET resposta = :resposta WHERE id = :id")
    suspend fun responder(id: Long, resposta: String)
}
