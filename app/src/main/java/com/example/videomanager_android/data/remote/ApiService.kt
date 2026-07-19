package com.example.videomanager_android.data.remote

import com.example.videomanager_android.data.model.Auth.LoginRequest
import com.example.videomanager_android.data.model.ResponseModel
import com.example.videomanager_android.data.model.UsuarioDados
import com.example.videomanager_android.data.model.Usuarios.UsuarioRequest
import com.example.videomanager_android.data.model.Videos.VideoDados
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @POST("api/Usuario/AdicionarUsuario")
    suspend fun  cadastrarUsuario(
        @Body request: UsuarioRequest
    ): retrofit2.Response<ResponseModel<UsuarioDados>>

    @POST("api/Auth/Login")
    suspend fun loginUsuario(
        @Body request: LoginRequest
    ): Response<ResponseModel<String>>

    @GET("api/Usuario/BuscarUsuarioPorID/{usuarioId}")
    suspend fun obterUsuarioPorId(
        @Header("Authorization") token: String,
        @Path("usuarioId") id: String
    ): Response<ResponseModel<UsuarioDados>>

    @GET("api/Videos/BuscarVideosPorUsuarioID/{usuarioID}")
    suspend fun obterVideosPorUsuarioId(
        @Header("Authorization") token: String,
        @Path("usuarioID") id: String
    ): Response<ResponseModel<List<VideoDados>>>
}