package com.inventario.app.ui.movimiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
