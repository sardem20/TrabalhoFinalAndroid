package com.example.arablecoreapi.data

import com.example.arablecoreapi.data.local.FavoriteDao
import com.example.arablecoreapi.data.local.FavoriteEntity
import com.example.arablecoreapi.data.remote.Especie
import com.example.arablecoreapi.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val api: ApiService, private val dao: FavoriteDao) {

    // withContext(IO) garante que a internet nunca roda na Main Thread.
    suspend fun buscar(texto: String): List<Especie> =
        withContext(Dispatchers.IO) { api.buscar(texto).results }

    suspend fun detalhe(key: Int): Especie =
        withContext(Dispatchers.IO) { api.detalhe(key) }

    suspend fun favoritos(userId: Long): List<FavoriteEntity> = dao.listar(userId)

    suspend fun alternarFavorito(userId: Long, especie: Especie) {
        if (dao.contar(userId, especie.key) > 0) {
            dao.remover(userId, especie.key)
        } else {
            dao.inserir(FavoriteEntity(userId, especie.key, especie.nome, especie.family))
        }
    }

    suspend fun removerFavorito(userId: Long, key: Int) = dao.remover(userId, key)
}
