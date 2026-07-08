package com.example.videomanager_android.data.model

data class ResponseModel<T>(
    val dados: T?,
    val mensagem: String,
    val status: Boolean
)

data class UsuarioDados(
    val id: Int,
    val nome: String,
    val email: String
)
