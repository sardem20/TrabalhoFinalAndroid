package com.example.arablecoreapi.data

import android.content.Context

// Guarda só o id de quem está logado, para continuar logado ao reabrir o app.
class UserSessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("sessao", Context.MODE_PRIVATE)

    fun salvar(id: Long) = prefs.edit().putLong("user_id", id).apply()

    fun usuarioLogado(): Long? = prefs.getLong("user_id", -1L).takeIf { it != -1L }

    fun limpar() = prefs.edit().remove("user_id").apply()
}
