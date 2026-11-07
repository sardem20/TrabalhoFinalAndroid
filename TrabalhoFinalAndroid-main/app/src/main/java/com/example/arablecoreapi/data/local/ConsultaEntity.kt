package com.example.arablecoreapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Uma pergunta do fazendeiro para um especialista. "resposta" fica null até o especialista responder.
@Entity(tableName = "consultas")
data class ConsultaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fazendeiroId: Long,
    val fazendeiroNome: String,
    val especialistaId: Long,
    val especialistaNome: String,
    val especialistaTipo: String,
    val pergunta: String,
    val resposta: String? = null
)
