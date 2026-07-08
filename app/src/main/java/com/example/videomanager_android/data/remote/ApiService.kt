package com.example.videomanager_android.data.remote

import com.example.videomanager_android.data.model.Auth.LoginRequest
import com.example.videomanager_android.data.model.ResponseModel
import com.example.videomanager_android.data.model.UsuarioDados
import com.example.videomanager_android.data.model.Usuarios.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/Usuario/AdicionarUsuario")
    suspend fun  cadastrarUsuario(
        @Body request: UsuarioRequest
    ): retrofit2.Response<ResponseModel<UsuarioDados>>

    @POST("api/Auth/Login")
    suspend fun loginUsuario(
        @Body request: LoginRequest
    ): Response<ResponseModel<String>>
}