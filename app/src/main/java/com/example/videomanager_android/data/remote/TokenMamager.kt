package com.example.videomanager_android.data.remote

object TokenManager {
    private var token: String? = null

    fun salvarToken(novoToken: String) {
        token = novoToken
    }

    fun obterToken(): String? {
        return token
    }

    fun obterHeaderAutorizacao(): String {
        return "Bearer ${token ?: ""}"
    }

    fun obterUsuarioId(): String {
        val tokenAtivo = token ?: return "0"
        return JwtUtils.extrairUsuarioIdDoToken(tokenAtivo)
    }

    fun limpar() {
        token = null
    }
}