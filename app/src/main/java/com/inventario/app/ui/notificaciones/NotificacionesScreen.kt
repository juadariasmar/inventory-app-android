package com.inventario.app.ui.notificaciones

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inventario.app.domain.model.Notificacion
import com.inventario.app.ui.components.NotificationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToLink: (String) -> Unit = {}
) {
    val mockNotificaciones = listOf(
        Notificacion(1, "STOCK_BAJO", "Stock bajo: Cable HDMI", "Quedan 3 unidades.", null, false, "2024-07-09T10:30:00Z"),
        Notificacion(2, "PRODUCTO_CREADO", "Nuevo producto creado", "Widget ABC fue agregado por Juan.", null, false, "2024-07-08T14:00:00Z"),
        Notificacion(3, "INVITACION_ACEPTADA", "María aceptó la invitación", "Ya es miembro de Bodega Principal.", null, true, "2024-07-07T09:00:00Z"),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { /* marcar todas como leídas */ }) {
                        Text("Leer todo")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(mockNotificaciones, key = { it.id }) { notificacion ->
                NotificationCard(
                    notificacion = notificacion,
                    onClick = { onNavigateToLink(notificacion.link ?: "") }
                )
            }
        }
    }
}
