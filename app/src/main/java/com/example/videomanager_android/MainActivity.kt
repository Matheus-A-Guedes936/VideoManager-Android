package com.example.videomanager_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videomanager_android.ui.screens.CadastroScreen
import com.example.videomanager_android.ui.screens.LoginScreen
import com.example.videomanager_android.ui.screens.HomeScreen
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {


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
            HomeScreen()
        }
    }
}