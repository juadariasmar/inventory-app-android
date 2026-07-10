package com.inventario.app.ui.producto

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.core.util.CurrencyUtils
import com.inventario.app.domain.model.Producto
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.ShimmerProductList
import com.inventario.app.ui.components.StockBadge
import com.inventario.app.ui.theme.Error
import com.inventario.app.ui.theme.Primary
import com.inventario.app.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onNavigateToForm: (Int?) -> Unit = {},
    onNavigateToCategorias: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ProductoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredProductos = remember(uiState.allProductos, uiState.searchQuery, uiState.selectedCategoriaId) {
        uiState.allProductos.filter { producto ->
            val matchesSearch = uiState.searchQuery.isBlank() ||
                    producto.nombre.contains(uiState.searchQuery, ignoreCase = true) ||
                    producto.codigo.contains(uiState.searchQuery, ignoreCase = true)
            val matchesCategory = uiState.selectedCategoriaId == null ||
                    producto.categoriaId == uiState.selectedCategoriaId
            matchesSearch && matchesCategory
        }
    }

    var showDeleteConfirm by remember { mutableStateOf<Pair<Int, String>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCategorias) {
                        Icon(
                            imageVector = Icons.Filled.Category,
                            contentDescription = "Categorías"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ErrorBanner(
                error = uiState.error,
                onDismiss = viewModel::onDismissError
            )

            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged
            )

            CategoryFilterRow(
                categorias = uiState.categorias,
                selectedId = uiState.selectedCategoriaId,
                onSelected = viewModel::onCategoryFilterChanged
            )

            PullToRefreshBox(
                isRefreshing = uiState.isLoading && uiState.allProductos.isNotEmpty(),
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.isLoading && uiState.allProductos.isEmpty() -> {
                        ShimmerProductList()
                    }
                    filteredProductos.isEmpty() && !uiState.isLoading -> {
                        EmptyState(
                            icon = Icons.Filled.Inventory,
                            title = "No hay productos",
                            description = "Crea tu primer producto para comenzar a gestionar tu inventario.",
                            actionText = "Crear producto",
                            onAction = { onNavigateToForm(null) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        ProductList(
                            productos = filteredProductos,
                            onProductClick = { onNavigateToForm(it.id) },
                            onDeleteRequest = { producto ->
                                showDeleteConfirm = producto.id to producto.nombre
                            }
                        )
                    }
                }
            }
        }
    }

    showDeleteConfirm?.let { (id, nombre) ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Estás seguro de que deseas eliminar \"$nombre\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDelete(id)
                    showDeleteConfirm = null
                }) {
                    Text("Eliminar", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics { contentDescription = "Buscar productos" },
        placeholder = { Text("Buscar por nombre o código...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun CategoryFilterRow(
    categorias: List<com.inventario.app.domain.model.Categoria>,
    selectedId: Int?,
    onSelected: (Int?) -> Unit
) {
    if (categorias.isEmpty()) return

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedId == null,
                onClick = { onSelected(null) },
                label = { Text("Todas") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary.copy(alpha = 0.12f)
                )
            )
        }
        items(categorias, key = { it.id }) { categoria ->
            FilterChip(
                selected = selectedId == categoria.id,
                onClick = {
                    onSelected(if (selectedId == categoria.id) null else categoria.id)
                },
                label = { Text(categoria.nombre) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary.copy(alpha = 0.12f)
                )
            )
        }
    }
}

@Composable
private fun ProductList(
    productos: List<Producto>,
    onProductClick: (Producto) -> Unit,
    onDeleteRequest: (Producto) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productos, key = { it.id }) { producto ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        onDeleteRequest(producto)
                        false
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar producto",
                            tint = Error
                        )
                    }
                },
                enableDismissFromStartToEnd = false
            ) {
                ProductCard(
                    producto = producto,
                    onClick = { onProductClick(producto) }
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    producto: Producto,
    onClick: () -> Unit
) {
    val isLowStock = producto.cantidad <= producto.stockMinimo

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "${producto.nombre}, código ${producto.codigo}, " +
                        "stock ${producto.cantidad}, precio ${CurrencyUtils.formatCOP(producto.precio)}"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (isLowStock) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Stock bajo",
                                tint = Warning,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = producto.categoriaNombre ?: "Sin categoría",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = producto.codigo,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StockBadge(
                    stock = producto.cantidad,
                    stockMinimo = producto.stockMinimo
                )
                Text(
                    text = CurrencyUtils.formatCOP(producto.precio),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
