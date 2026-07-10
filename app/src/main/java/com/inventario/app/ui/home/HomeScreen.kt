package com.inventario.app.ui.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.core.util.CurrencyUtils
import com.inventario.app.core.util.DateUtils
import com.inventario.app.domain.model.DashboardMetrics
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.InventarioDrawerContent
import com.inventario.app.ui.components.MetricCard
import com.inventario.app.ui.components.ShimmerDashboardGrid
import com.inventario.app.ui.components.ShimmerMovementList
import com.inventario.app.ui.theme.Error
import com.inventario.app.ui.theme.Primary
import com.inventario.app.ui.theme.Success
import com.inventario.app.ui.theme.Warning
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProductos: () -> Unit = {},
    onNavigateToMovimientos: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                InventarioDrawerContent(
                    userName = "Juan Pérez",
                    userEmail = "juan@empresa.com",
                    currentWs = "Bodega Principal",
                    currentOrg = "Mi Empresa",
                    onNavigateTo = { label ->
                        scope.launch { drawerState.close() }
                        when (label) {
                            "Productos" -> onNavigateToProductos()
                            "Movimientos" -> onNavigateToMovimientos()
                        }
                    },
                    onLogout = { onNavigateToSettings() }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: notifications */ }) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ) {
                                        Text("3")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notificaciones"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { padding ->
            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = viewModel::refresh,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorBanner(
                        error = uiState.error,
                        onDismiss = viewModel::refresh
                    )

                    when {
                        uiState.isLoading && uiState.metrics == null -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    ShimmerDashboardGrid()
                                }
                                item {
                                    Text(
                                        text = "Últimos Movimientos",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                                item {
                                    ShimmerMovementList()
                                }
                            }
                        }
                        uiState.metrics != null -> {
                            DashboardContent(
                                metrics = uiState.metrics!!,
                                onRefresh = viewModel::refresh,
                                onNavigateToProductos = onNavigateToProductos,
                                onNavigateToMovimientos = onNavigateToMovimientos
                            )
                        }
                        else -> {
                            EmptyState(
                                icon = Icons.Filled.ErrorOutline,
                                title = "Sin datos",
                                description = "No se pudieron cargar las métricas del dashboard.",
                                actionText = "Reintentar",
                                onAction = viewModel::refresh,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    metrics: DashboardMetrics,
    onRefresh: () -> Unit,
    onNavigateToProductos: () -> Unit,
    onNavigateToMovimientos: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                item {
                    MetricCard(
                        icon = Icons.Filled.Inventory,
                        value = metrics.totalProductos.toString(),
                        label = "Productos",
                        containerColor = Primary.copy(alpha = 0.12f),
                        onClick = onNavigateToProductos
                    )
                }
                item {
                    MetricCard(
                        icon = Icons.Filled.TrendingUp,
                        value = metrics.totalUnidades.toString(),
                        label = "Unidades",
                        containerColor = Success.copy(alpha = 0.12f)
                    )
                }
                item {
                    MetricCard(
                        icon = Icons.Filled.ErrorOutline,
                        value = metrics.stockBajo.toString(),
                        label = "Stock Bajo",
                        containerColor = if (metrics.stockBajo > 0) Error.copy(alpha = 0.12f) else Warning.copy(alpha = 0.12f)
                    )
                }
                item {
                    MetricCard(
                        icon = Icons.Filled.AttachMoney,
                        value = CurrencyUtils.formatCOP(metrics.valorInventario),
                        label = "Valor Inventario",
                        containerColor = Primary.copy(alpha = 0.12f)
                    )
                }
            }
        }

        item {
            Text(
                text = "Últimos Movimientos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (metrics.ultimosMovimientos.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Inventory,
                    title = "Sin movimientos recientes",
                    description = "Los últimos movimientos aparecerán aquí.",
                    modifier = Modifier.height(200.dp)
                )
            }
        } else {
            items(
                items = metrics.ultimosMovimientos.take(10),
                key = { it.id }
            ) { movimiento ->
                MovementItem(movimiento = movimiento)
            }
        }
    }
}

@Composable
private fun MovementItem(movimiento: Movimiento) {
    val icon: ImageVector = when (movimiento.tipo) {
        TipoMovimiento.entrada -> Icons.Filled.TrendingUp
        TipoMovimiento.salida -> Icons.Filled.TrendingDown
    }

    val iconColor = when (movimiento.tipo) {
        TipoMovimiento.entrada -> Success
        TipoMovimiento.salida -> Error
    }

    val tipoText = when (movimiento.tipo) {
        TipoMovimiento.entrada -> "Entrada"
        TipoMovimiento.salida -> "Salida"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "$tipoText: ${movimiento.productoNombre}, cantidad ${movimiento.cantidad}" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movimiento.productoNombre ?: "Producto desconocido",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$tipoText · ${movimiento.usuarioNombre ?: "Sistema"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (movimiento.tipo == TipoMovimiento.entrada) "+${movimiento.cantidad}" else "-${movimiento.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
                Text(
                    text = DateUtils.formatTime(movimiento.creadoEn),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
