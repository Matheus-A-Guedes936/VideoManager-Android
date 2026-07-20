package com.example.videomanager_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderPrincipal(
    titulo: String,
    onPerfilClick: () -> Unit,
    onVideosClick: () -> Unit,
    onSairClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onPerfilClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil / Home",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },

        actions = {
            IconButton(onClick = onVideosClick) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Vídeos",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            IconButton(onClick = onSairClick) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Sair do App",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}