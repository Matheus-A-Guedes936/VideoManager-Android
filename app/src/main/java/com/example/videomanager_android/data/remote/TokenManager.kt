package com.example.videomanager_android.data.remote

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREF_NAME = "VideoManagerPrefs"
    private const val KEY_TOKEN = "jwt_token"

    private var sharedPreferences: SharedPreferences? = null

    fun inicializar(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }


    fun salvarToken(novoToken: String) {
        sharedPreferences?.edit()?.apply {
            putString(KEY_TOKEN, novoToken)
            apply()
        }
    }

    fun obterToken(): String? {
        return sharedPreferences?.getString(KEY_TOKEN, null)
    }

    fun obterHeaderAutorizacao(): String {
        val tokenAtivo = obterToken() ?: ""
        return "Bearer $tokenAtivo"
    }

    fun obterUsuarioId(): String {
        val tokenAtivo = obterToken() ?: return "0"
        return JwtUtils.extrairUsuarioIdDoToken(tokenAtivo)
    }

    fun limpar() {
        sharedPreferences?.edit()?.apply {
            remove(KEY_TOKEN)
            apply()
        }
    }
}