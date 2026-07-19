package com.example.videomanager_android.ui.screens

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
import com.example.videomanager_android.data.model.Usuarios.UsuarioRequest
import com.example.videomanager_android.data.remote.di.NetworkModule
import com.example.videomanager_android.ui.theme.VideoManagerAndroidTheme
import kotlinx.coroutines.launch

@Composable
fun CadastroScreen(
    onNavigateBack: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope ()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cadastro", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val request = UsuarioRequest(nome = nome, email = email, senha = senha)
                        val response = NetworkModule.apiService.cadastrarUsuario(request)

                        if (response.isSuccessful && response.body()?.status == true) {
                            val msgSucesso = response.body()?.mensagem ?: "Sucesso!"
                            Toast.makeText(context, msgSucesso, Toast.LENGTH_LONG).show()
                            onNavigateBack()
                        } else {
                            val msgErro = response.body()?.mensagem ?: "Erro na validação dos dados."
                            Toast.makeText(context, msgErro, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Falha na conexão: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Já tem conta? Faça Login")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    VideoManagerAndroidTheme {
        CadastroScreen(onNavigateBack = {})
    }
}