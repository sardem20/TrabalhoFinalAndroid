package com.example.arablecoreapi.data

import android.util.Patterns
import com.example.arablecoreapi.data.local.UserDao
import com.example.arablecoreapi.data.local.UserEntity

private fun String.sha256(): String =
    java.security.MessageDigest.getInstance("SHA-256")
        .digest(toByteArray()).joinToString("") { "%02x".format(it) }

// Em caso de erro, lança Exception com a mensagem que aparece na tela.
class UserRepository(private val dao: UserDao) {

    suspend fun cadastrar(nome: String, email: String, senha: String): UserEntity {
        val emailLimpo = email.trim().lowercase()
        if (nome.isBlank()) throw Exception("Informe seu nome.")
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpo).matches()) throw Exception("Informe um e-mail válido.")
        if (senha.length < 6) throw Exception("A senha precisa ter 6 ou mais caracteres.")
        if (dao.buscarPorEmail(emailLimpo) != null) throw Exception("Este e-mail já está cadastrado.")

        val novo = UserEntity(nome = nome.trim(), email = emailLimpo, senhaHash = senha.sha256())
        return novo.copy(id = dao.inserir(novo))
    }

    suspend fun entrar(email: String, senha: String): UserEntity {
        val usuario = dao.buscarPorEmail(email.trim().lowercase())
        if (usuario == null || usuario.senhaHash != senha.sha256()) {
            throw Exception("E-mail ou senha incorretos.") // não diz qual dos dois errou
        }
        return usuario
    }

    suspend fun buscarPorId(id: Long): UserEntity? = dao.buscarPorId(id)
}
