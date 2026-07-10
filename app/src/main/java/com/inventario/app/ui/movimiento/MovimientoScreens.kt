package com.inventario.app.ui.movimiento

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inventario.app.core.util.DateUtils
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.ShimmerMovementList
import com.inventario.app.ui.theme.Error
import com.inventario.app.ui.theme.ErrorContainer
import com.inventario.app.ui.theme.Success
import com.inventario.app.ui.theme.SuccessContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientosScreen(
    onNavigateToForm: (String) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: MovimientoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimientos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.filtroTipo == null,
                    onClick = { viewModel.onFilterChanged(null) },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = uiState.filtroTipo == TipoMovimiento.entrada,
                    onClick = { viewModel.onFilterChanged(TipoMovimiento.entrada) },
                    label = { Text("Entradas") }
                )
                FilterChip(
                    selected = uiState.filtroTipo == TipoMovimiento.salida,
                    onClick = { viewModel.onFilterChanged(TipoMovimiento.salida) },
                    label = { Text("Salidas") }
                )
            }

            ErrorBanner(
                error = uiState.error,
                onDismiss = { viewModel.onDismissError() }
            )

            PullToRefreshBox(
                isRefreshing = uiState.isLoading && uiState.movimientos.isNotEmpty(),
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.isLoading && uiState.movimientos.isEmpty() -> {
                        ShimmerMovementList()
                    }
                    uiState.movimientos.isEmpty() -> {
                        EmptyState(
                            icon = Icons.Filled.SwapHoriz,
                            title = "Sin movimientos",
                            description = "Registra una entrada o salida para comenzar",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 0.dp, end = 0.dp, top = 4.dp, bottom = 88.dp
                            )
                        ) {
                            items(uiState.movimientos, key = { it.id }) { movimiento ->
                                MovimientoCard(movimiento = movimiento)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientoFormScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: MovimientoFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navEvent by viewModel.navigationEvent.collectAsStateWithLifecycle(initialValue = null)
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(navEvent) {
        if (navEvent == true) {
            Toast.makeText(context, "Movimiento registrado", Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Descartar cambios") },
            text = { Text("¿Descartar el movimiento? Los cambios no se guardarán.") },
            confirmButton = { TextButton(onClick = { onNavigateBack() }) { Text("Descartar") } },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text("Continuar") } }
        )
    }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Movimiento") },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Tipo selector
            Text("Tipo:", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.tipo == TipoMovimiento.entrada,
                    onClick = { viewModel.onTipoChanged(TipoMovimiento.entrada) },
                    label = { Text("↓ ENTRADA") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = uiState.tipo == TipoMovimiento.salida,
                    onClick = { viewModel.onTipoChanged(TipoMovimiento.salida) },
                    label = { Text("SALIDA ↑") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Producto selector
            Text("Producto*:", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            androidx.compose.material3.OutlinedTextField(
                value = uiState.selectedProducto?.let { "${it.nombre} (Stock: ${it.cantidad})" } ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = { Text("Producto seleccionado") },
                trailingIcon = {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            )
            uiState.fieldErrors["producto"]?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // Quick product list
            LazyColumn(modifier = Modifier.height(150.dp)) {
                items(uiState.productos.take(20)) { producto ->
                    val isSelected = uiState.selectedProducto?.id == producto.id
                    Surface(
                        onClick = { viewModel.onProductoSelected(producto) },
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clip(MaterialTheme.shapes.small)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(producto.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            if (producto.cantidad <= producto.stockMinimo) {
                                Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                            }
                            Text("(${producto.cantidad})", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Cantidad
            OutlinedTextField(
                value = uiState.cantidad,
                onValueChange = viewModel::onCantidadChanged,
                label = { Text("Cantidad*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.fieldErrors.containsKey("cantidad"),
                supportingText = uiState.fieldErrors["cantidad"]?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Spacer(Modifier.height(12.dp))

            // Notas
            OutlinedTextField(
                value = uiState.notas,
                onValueChange = viewModel::onNotasChanged,
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2, maxLines = 4
            )

            Spacer(Modifier.height(16.dp))

            // Stock preview
            val stockActual = uiState.selectedProducto?.cantidad ?: 0
            val cantidadInt = uiState.cantidad.toIntOrNull() ?: 0
            val stockDespues = if (uiState.tipo == TipoMovimiento.entrada) stockActual + cantidadInt
                               else stockActual - cantidadInt
            val esValido = stockDespues >= 0 && cantidadInt > 0

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (esValido) SuccessContainer.copy(alpha = 0.3f)
                                    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Stock actual: $stockActual", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Stock después: ${if (cantidadInt > 0) stockDespues else "—"} ${if (esValido) "✅" else "❌"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Error
            uiState.error?.let { error ->
                ErrorBanner(error = error, onDismiss = viewModel::onDismissError)
                Spacer(Modifier.height(8.dp))
            }

            // Register button
            Button(
                onClick = viewModel::onRegistrar,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !uiState.isLoading && uiState.selectedProducto != null && uiState.cantidad.toIntOrNull() != null
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrar")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun MovimientoCard(movimiento: Movimiento) {
    val isEntrada = movimiento.tipo == TipoMovimiento.entrada
    val containerColor = if (isEntrada) SuccessContainer else ErrorContainer
    val icon = if (isEntrada) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward
    val iconTint = if (isEntrada) Success else Error

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = containerColor,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buildString {
                        append(if (isEntrada) "Entrada" else "Salida")
                        append("  x${movimiento.cantidad}")
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = movimiento.productoNombre ?: "Producto #${movimiento.productoId}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildString {
                        movimiento.usuarioNombre?.let { append("$it · ") }
                        append(DateUtils.formatTime(movimiento.creadoEn))
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
