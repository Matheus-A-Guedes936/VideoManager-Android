package com.example.videomanager_android.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.videomanager_android.data.model.Auth.LoginRequest
import com.example.videomanager_android.data.model.ResponseModel
import com.example.videomanager_android.data.remote.di.NetworkModule
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToCadastro: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope ()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val request = LoginRequest(email = email, senha = senha)

                        val response = NetworkModule.apiService.loginUsuario(request)

                        if (response.isSuccessful && response.body()?.status == true) {
                            val mensagemSucesso = response.body()?.mensagem ?: "Bem-vindo!"
                            val token = response.body()?.dados


                            Toast.makeText(context, token ?: "Token nulo", Toast.LENGTH_LONG).show()

                            // onNavigateToHome()

                        } else {
                            val erroBruto = response.errorBody()?.string()

                            val mensagemErro = try {
                                val erroConvertido = com.google.gson.Gson().fromJson(erroBruto, ResponseModel::class.java)
                                erroConvertido?.mensagem ?: "E-mail ou senha incorretos."
                            } catch (e: Exception) {
                                "Erro no servidor: ${response.code()}"
                            }

                            Toast.makeText(context, mensagemErro, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Falha na conexão: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToCadastro) {
            Text("Não tem uma conta? Cadastre-se")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    VideoManagerAndroidTheme{
        LoginScreen()
    }
}