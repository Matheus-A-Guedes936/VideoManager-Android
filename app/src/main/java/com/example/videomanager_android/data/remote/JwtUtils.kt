package com.example.videomanager_android.data.remote

import android.util.Base64
import android.util.Log
import org.json.JSONObject

object JwtUtils {

    fun extrairUsuarioIdDoToken(token: String): String {
        return try {
            val partes = token.split(".")
            if (partes.size < 2) return "0"

            val payloadBase64 = partes[1]
            val bytesDecodificados = Base64.decode(payloadBase64, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            val jsonString = String(bytesDecodificados, Charsets.UTF_8)

            val jsonObject = JSONObject(jsonString)

            if (jsonObject.has("nameid")) {
                jsonObject.getString("nameid")
            } else {
                "0"
            }

        } catch (e: Exception) {
            Log.e("JWT_DECODE", "Erro ao extrair ID do token: ${e.localizedMessage}")
            "0"
        }
    }
    fun isTokenExpirado(token: String): Boolean {
        return try {
            val partes = token.split(".")
            if (partes.size < 2) return true

            val payloadBase64 = partes[1]
            val bytesDecodificados = Base64.decode(payloadBase64, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            val jsonString = String(bytesDecodificados, Charsets.UTF_8)

            val jsonObject = JSONObject(jsonString)

            if (jsonObject.has("exp")) {
                val tempoExpiracaoSegundos = jsonObject.getLong("exp")

                val tempoAtualSegundos = System.currentTimeMillis() / 1000

                tempoAtualSegundos >= tempoExpiracaoSegundos
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("JWT_DECODE", "Erro ao verificar expiração do token: ${e.localizedMessage}")
            true
        }
    }
}

