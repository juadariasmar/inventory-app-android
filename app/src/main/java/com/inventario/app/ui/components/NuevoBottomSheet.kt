package com.inventario.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NuevoBottomSheet(
    onNuevoProducto: () -> Unit,
    onNuevoMovimiento: () -> Unit,
    onNuevaVenta: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        ListItem(
            headlineContent = { Text("Nuevo Producto") },
            supportingContent = { Text("Crear producto en el inventario") },
            leadingContent = {
                Icon(
                    Icons.Filled.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.clickable { onNuevoProducto() }
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Nuevo Movimiento") },
            supportingContent = { Text("Registrar entrada o salida de stock") },
            leadingContent = {
                Icon(
                    Icons.Filled.SwapHoriz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.clickable { onNuevoMovimiento() }
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Nueva Venta") },
            supportingContent = { Text("Crear venta con productos") },
            leadingContent = {
                Icon(
                    Icons.Filled.PointOfSale,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.clickable { onNuevaVenta() }
        )
    }
}
