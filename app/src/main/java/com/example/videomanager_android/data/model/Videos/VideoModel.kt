package com.example.videomanager_android.data.model.Videos

data class VideoDados(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val caminhoVideo: String,
    val caminhoVideoThumbnail: String,
    val usuarioID: Int
)