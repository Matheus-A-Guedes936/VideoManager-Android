package com.example.videomanager_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videomanager_android.ui.cadastro.CadastroScreen
import com.example.videomanager_android.ui.login.LoginScreen
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoManagerAndroidTheme {
                VideoManagerAndroidTheme {

                    VideoManagerAppNavigation()

                }
            }
        }
    }
}

@Composable
fun VideoManagerAppNavigation() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "login") {

        // Define a rota da tela de Login
        composable("login") {
            LoginScreen(
                onNavigateToCadastro = {
                    navController.navigate("cadastro")
                }
            )
        }

        // Define a rota da tela de Cadastro
        composable("cadastro") {
            CadastroScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}