package com.example.videomanager_android.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.videomanager_android.data.model.Videos.VideoDados
import com.example.videomanager_android.data.remote.TokenManager
import com.example.videomanager_android.data.remote.di.NetworkConfig
import com.example.videomanager_android.data.remote.di.NetworkModule
import com.example.videomanager_android.ui.components.HeaderPrincipal
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {

    var pesquisa by remember { mutableStateOf("") }
    var nomeUsuario by remember { mutableStateOf("Carregando...") }
    var emailUsuario by remember { mutableStateOf("Carregando...") }

    var listaVideos by remember {mutableStateOf<List<VideoDados>>(emptyList())}
    var carregarVideos by remember { mutableStateOf(true) }

    val baseUrlApi = NetworkConfig.baseUrlApi
    val context = androidx.compose.ui.platform.LocalContext.current


    LaunchedEffect(Unit) {
        try {
            val userId = TokenManager.obterUsuarioId()
            val tokenAutorizacao = TokenManager.obterHeaderAutorizacao()

            if (userId != "0" && userId.isNotEmpty()) {

                try {
                    val response = NetworkModule.apiService.obterUsuarioPorId(
                        token = tokenAutorizacao,
                        id = userId
                    )

                    if (response.isSuccessful && response.body()?.status == true) {
                        val usuario = response.body()?.dados
                        if (usuario != null) {
                            nomeUsuario = usuario.nome
                            emailUsuario = usuario.email
                        } else {
                            nomeUsuario = "Dados Nulos"
                            emailUsuario = "O campo dados retornou vazio"
                        }
                    } else {
                        nomeUsuario = "Erro de API"
                        val erroMsg = response.body()?.mensagem ?: "Código: ${response.code()}"
                        emailUsuario = erroMsg
                    }
                } catch (e: Exception) {
                    nomeUsuario = "Erro ao carregar perfil"
                    emailUsuario = "Falha na requisição do usuário"
                    e.printStackTrace()
                }

                try {
                    val videosResponse = NetworkModule.apiService.obterVideosPorUsuarioId(
                        token = tokenAutorizacao,
                        id = userId
                    )
                    if (videosResponse.isSuccessful && videosResponse.body()?.status == true) {
                        listaVideos = videosResponse.body()?.dados ?: emptyList()
                    } else {
                        val erroVideos = videosResponse.body()?.mensagem ?: "Código: ${videosResponse.code()}"
                        android.widget.Toast.makeText(context, "Erro nos Vídeos: $erroVideos", android.widget.Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    android.widget.Toast.makeText(context, "Falha de rede ao buscar vídeos", android.widget.Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            } else {
                nomeUsuario = "Token Inválido"
                emailUsuario = "Erro Token"
                android.widget.Toast.makeText(context, "Erro: ID extraído do token está zerado!", android.widget.Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            nomeUsuario = "Erro Crítico"
            emailUsuario = "Falha geral no carregamento"
        } finally {
            carregarVideos = false
        }
    }


    Scaffold(
        topBar = {
            HeaderPrincipal(
                titulo = "VideoManager",
                onPerfilClick = {
                    android.widget.Toast.makeText(context, "Você já está no seu Perfil!", android.widget.Toast.LENGTH_SHORT).show()
                },
                onVideosClick = {

                    android.widget.Toast.makeText(context, "", android.widget.Toast.LENGTH_SHORT).show()
                },
                onSairClick = {
                    TokenManager.limpar()

                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                    android.widget.Toast.makeText(context, "Sessão encerrada com sucesso", android.widget.Toast.LENGTH_LONG).show()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                val primeiraLetra = if (nomeUsuario.isNotEmpty() && nomeUsuario != "Carregando...") {
                    nomeUsuario.trim().first().uppercase()
                } else {
                    "M"
                }

                Text(
                    text = primeiraLetra,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(text = nomeUsuario, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = emailUsuario, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text(text = "${listaVideos.size} Vídeos", fontWeight = FontWeight.Bold)
                    Text(text = "0 Visualizações", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = pesquisa,
                onValueChange = { pesquisa = it },
                placeholder = { Text("Pesquisar Meus Videos...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val videosFiltrados = listaVideos.filter {
                it.titulo.contains(pesquisa, ignoreCase = true)
            }

            if (carregarVideos) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            } else if (videosFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum vídeo encontrado.", color = Color.Gray)
                }
            } else
                {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ){
                    items(videosFiltrados) { video ->

                        val tokenParaImagem = remember { TokenManager.obterHeaderAutorizacao() }

                        val urlFinalThumbnail = if (baseUrlApi.endsWith("/") || video.caminhoVideoThumbnail.startsWith("/")) {
                            "$baseUrlApi${video.caminhoVideoThumbnail}"
                        } else {
                            "$baseUrlApi/${video.caminhoVideoThumbnail}"
                        }


                        Card(
                            onClick = {
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(175.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.LightGray.copy(alpha = 0.5f)
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {
                            Column {
                                coil3.compose.AsyncImage(
                                    model = urlFinalThumbnail,
                                    imageLoader = remember(tokenParaImagem) {
                                        coil3.ImageLoader.Builder(context)
                                            .components {
                                                add(coil3.network.okhttp.OkHttpNetworkFetcherFactory(
                                                    callFactory = {
                                                        okhttp3.OkHttpClient.Builder()
                                                            .addInterceptor { chain ->
                                                                val novaRequisicao = chain.request().newBuilder()
                                                                    .addHeader("Authorization", tokenParaImagem)
                                                                    .build()
                                                                chain.proceed(novaRequisicao)
                                                            }
                                                            .build()
                                                    }
                                                ))
                                            }
                                            .build()
                                    },
                                    contentDescription = "Miniatura do vídeo: ${video.titulo}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(115.dp),
                                    contentScale = ContentScale.Crop,
                                    onState = { state ->
                                        if (state is coil3.compose.AsyncImagePainter.State.Error) {
                                            val erroCoil = state.result.throwable
                                            android.util.Log.e("COIL_ERROR", "Falha ao carregar imagem", erroCoil)
                                            android.widget.Toast.makeText(
                                                context,
                                                "Erro Imagem: ${erroCoil.localizedMessage}",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = video.titulo,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "👁",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 1.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "0",
                                            fontSize = 11.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {

    val navControllerFake = rememberNavController()

    VideoManagerAndroidTheme {
        HomeScreen(navController = navControllerFake)
    }
}