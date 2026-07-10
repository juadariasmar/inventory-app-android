package com.inventario.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InventarioDrawerContent(
    userName: String,
    userEmail: String,
    currentWs: String,
    currentOrg: String,
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = userName.first().uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$currentOrg / $currentWs",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        val drawerItems = listOf(
            "Home" to Icons.Filled.Home,
            "Productos" to Icons.Filled.Inventory2,
            "Movimientos" to Icons.Filled.SwapHoriz,
            "Ventas" to Icons.Filled.PointOfSale,
            "Clientes" to Icons.Filled.People,
        )

        drawerItems.forEach { (label, icon) ->
            NavigationDrawerItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label) },
                selected = false,
                onClick = { onNavigateTo(label) }
            )
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
            selected = false,
            onClick = onLogout
        )
    }
}
