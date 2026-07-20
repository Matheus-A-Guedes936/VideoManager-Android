package com.example.videomanager_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videomanager_android.data.remote.JwtUtils
import com.example.videomanager_android.data.remote.TokenManager
import com.example.videomanager_android.ui.screens.CadastroScreen
import com.example.videomanager_android.ui.screens.LoginScreen
import com.example.videomanager_android.ui.screens.HomeScreen
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.inicializar(applicationContext)

        enableEdgeToEdge()
        setContent {
            VideoManagerAndroidTheme {

                    VideoManagerAppNavigation()
            }
        }
    }
}

@Composable
fun VideoManagerAppNavigation() {
    val token = TokenManager.obterToken()

    val navController = rememberNavController()

    val usuarioEstaLogado = !token.isNullOrEmpty() && !JwtUtils.isTokenExpirado(token)

    if (!token.isNullOrEmpty() && JwtUtils.isTokenExpirado(token)) {
        TokenManager.limpar()
    }

    val destinoInicial = if (usuarioEstaLogado) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = destinoInicial
    ) {


        composable("login") {
            LoginScreen(
                onNavigateToCadastro = {
                    navController.navigate("cadastro")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }


        composable("cadastro") {
            CadastroScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}