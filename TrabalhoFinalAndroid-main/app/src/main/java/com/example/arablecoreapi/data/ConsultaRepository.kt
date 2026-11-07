package com.example.arablecoreapi.data

import com.example.arablecoreapi.data.local.ConsultaDao
import com.example.arablecoreapi.data.local.ConsultaEntity
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.data.local.UserDao
import com.example.arablecoreapi.data.local.UserEntity

class ConsultaRepository(private val userDao: UserDao, private val dao: ConsultaDao) {

    suspend fun especialistas(): List<UserEntity> = userDao.listarEspecialistas()

    // Fazendeiro vê as perguntas que fez; especialista vê as que recebeu.
    suspend fun minhas(usuario: UserEntity): List<ConsultaEntity> =
        if (usuario.tipo == Tipo.FAZENDEIRO) dao.doFazendeiro(usuario.id)
        else dao.doEspecialista(usuario.id)

    suspend fun enviar(fazendeiro: UserEntity, especialista: UserEntity, pergunta: String) {
        dao.inserir(
            ConsultaEntity(
                fazendeiroId = fazendeiro.id,
                fazendeiroNome = fazendeiro.nome,
                especialistaId = especialista.id,
                especialistaNome = especialista.nome,
                especialistaTipo = especialista.tipo,
                pergunta = pergunta.trim()
            )
        )
    }

    suspend fun responder(id: Long, resposta: String) = dao.responder(id, resposta.trim())
}
