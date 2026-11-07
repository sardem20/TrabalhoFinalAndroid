package com.example.arablecoreapi.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.arablecoreapi.data.AppRepository
import com.example.arablecoreapi.data.ConsultaRepository
import com.example.arablecoreapi.data.UserRepository
import com.example.arablecoreapi.data.UserSessionManager
import com.example.arablecoreapi.data.local.AppDatabase
import com.example.arablecoreapi.data.local.ConsultaEntity
import com.example.arablecoreapi.data.local.FavoriteEntity
import com.example.arablecoreapi.data.local.UserEntity
import com.example.arablecoreapi.data.remote.Especie
import com.example.arablecoreapi.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.io.IOException

// Um único ViewModel guarda tudo que as telas mostram. Cada "var" abaixo é um estado:
// quando muda, a tela se atualiza sozinha.
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val session = UserSessionManager(app)
    private val banco = AppDatabase.getInstance(app)
    private val userRepo = UserRepository(banco.userDao())
    private val repo = AppRepository(RetrofitInstance.api, banco.favoriteDao())
    private val consultaRepo = ConsultaRepository(banco.userDao(), banco.consultaDao())

    // ---- Login ----
    var userId by mutableStateOf(session.usuarioLogado()); private set
    var usuario by mutableStateOf<UserEntity?>(null); private set
    var entrando by mutableStateOf(false); private set
    var erroLogin by mutableStateOf<String?>(null); private set

    // ---- Lista de espécies (API) ----
    var lista by mutableStateOf<List<Especie>>(emptyList()); private set
    var carregandoLista by mutableStateOf(false); private set
    var erroLista by mutableStateOf<String?>(null); private set
    var ultimaBusca = "Glycine max"; private set

    // ---- Detalhe (API) ----
    var especie by mutableStateOf<Especie?>(null); private set
    var carregandoDetalhe by mutableStateOf(false); private set
    var erroDetalhe by mutableStateOf<String?>(null); private set

    // ---- Favoritos (Room) ----
    var favoritos by mutableStateOf<List<FavoriteEntity>>(emptyList()); private set

    // ---- Consultas (Room) ----
    var especialistas by mutableStateOf<List<UserEntity>>(emptyList()); private set
    var consultas by mutableStateOf<List<ConsultaEntity>>(emptyList()); private set

    init {
        buscar(ultimaBusca)
        // Já estava logado? Carrega o usuário e os favoritos dele.
        userId?.let { id ->
            viewModelScope.launch {
                val u = userRepo.buscarPorId(id)
                if (u == null) sair() else {
                    usuario = u
                    favoritos = repo.favoritos(id)
                    especialistas = consultaRepo.especialistas()
                    consultas = consultaRepo.minhas(u)
                }
            }
        }
    }

    // ---------- Login ----------
    fun entrar(email: String, senha: String) = acessar { userRepo.entrar(email, senha) }

    fun cadastrar(nome: String, email: String, senha: String, tipo: String) =
        acessar { userRepo.cadastrar(nome, email, senha, tipo) }

    private fun acessar(acao: suspend () -> UserEntity) {
        viewModelScope.launch {
            entrando = true
            erroLogin = null
            try {
                val u = acao()
                session.salvar(u.id)
                usuario = u
                userId = u.id
                favoritos = repo.favoritos(u.id)
                especialistas = consultaRepo.especialistas()
                consultas = consultaRepo.minhas(u)
            } catch (e: Exception) {
                erroLogin = e.message
            }
            entrando = false
        }
    }

    fun limparErro() {
        erroLogin = null
    }

    fun sair() {
        session.limpar()
        userId = null
        usuario = null
        favoritos = emptyList()
        especialistas = emptyList()
        consultas = emptyList()
    }

    // ---------- API ----------
    fun buscar(texto: String) {
        if (texto.isBlank()) return
        ultimaBusca = texto.trim()
        viewModelScope.launch {
            carregandoLista = true
            erroLista = null
            try {
                lista = repo.buscar(ultimaBusca)
            } catch (e: Exception) {
                erroLista = mensagemDeErro(e)
            }
            carregandoLista = false
        }
    }

    fun carregarDetalhe(key: Int) {
        viewModelScope.launch {
            especie = null
            carregandoDetalhe = true
            erroDetalhe = null
            try {
                especie = repo.detalhe(key)
            } catch (e: Exception) {
                erroDetalhe = mensagemDeErro(e)
            }
            carregandoDetalhe = false
        }
    }

    private fun mensagemDeErro(e: Exception): String =
        if (e is IOException) "Sem conexão com a internet. Tente de novo."
        else "Não foi possível carregar os dados."

    // ---------- Favoritos ----------
    fun alternarFavorito(e: Especie) {
        val id = userId ?: return
        viewModelScope.launch {
            repo.alternarFavorito(id, e)
            favoritos = repo.favoritos(id)
        }
    }

    fun removerFavorito(key: Int) {
        val id = userId ?: return
        viewModelScope.launch {
            repo.removerFavorito(id, key)
            favoritos = repo.favoritos(id)
        }
    }

    // ---------- Consultas ----------
    fun enviarConsulta(especialista: UserEntity, pergunta: String) {
        val eu = usuario ?: return
        viewModelScope.launch {
            consultaRepo.enviar(eu, especialista, pergunta)
            consultas = consultaRepo.minhas(eu)
        }
    }

    fun responderConsulta(id: Long, resposta: String) {
        val eu = usuario ?: return
        viewModelScope.launch {
            consultaRepo.responder(id, resposta)
            consultas = consultaRepo.minhas(eu)
        }
    }
}
