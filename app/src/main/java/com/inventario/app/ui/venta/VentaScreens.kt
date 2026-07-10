package com.inventario.app.ui.venta

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.core.util.CurrencyUtils
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.Venta
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.LoadingOverlay
import com.inventario.app.ui.theme.Error
import com.inventario.app.ui.theme.Success
import com.inventario.app.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    onNavigateToForm: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: VentaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ventas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva Venta"
                )
            }
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

            when {
                uiState.isLoading -> LoadingOverlay(isLoading = true)
                uiState.ventas.isEmpty() -> EmptyState(
                    icon = Icons.Filled.PointOfSale,
                    title = "Sin ventas",
                    description = "Registra tu primera venta para comenzar.",
                    actionText = "Nueva Venta",
                    onAction = onNavigateToForm
                )
                else -> VentaList(
                    ventas = uiState.ventas,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun VentaList(
    ventas: List<Venta>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ventas, key = { it.id }) { venta ->
            VentaCard(venta = venta)
        }
    }
}

@Composable
private fun VentaCard(venta: Venta) {
    val isCancelada = venta.canceladaEn != null
    val badgeColor = if (isCancelada) Error else Success
    val badgeText = if (isCancelada) "CANCELADA" else "COMPLETADA"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Venta #${venta.id}, total ${CurrencyUtils.formatCOP(venta.total)}, $badgeText"
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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Venta #${venta.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .semantics { contentDescription = "Estado $badgeText" }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (venta.clienteNombre != null) {
                Text(
                    text = "Cliente: ${venta.clienteNombre}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (venta.vendedorNombre != null) {
                Text(
                    text = "Vendedor: ${venta.vendedorNombre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${venta.items.size} producto${if (venta.items.size != 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = CurrencyUtils.formatCOP(venta.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: VentaFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            snackbarHostState.showSnackbar("Venta registrada correctamente")
            onNavigateBack()
        }
    }

    var showAddItemDialog by remember { mutableStateOf(false) }
    var selectedClienteSearch by remember { mutableStateOf("") }
    var showClienteDropdown by remember { mutableStateOf(false) }

    val filteredClientes = remember(uiState.clientes, selectedClienteSearch) {
        if (selectedClienteSearch.isBlank()) uiState.clientes
        else uiState.clientes.filter {
            it.nombre.contains(selectedClienteSearch, ignoreCase = true) ||
                    it.documento?.contains(selectedClienteSearch, ignoreCase = true) == true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Venta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ErrorBanner(
                error = uiState.error,
                onDismiss = viewModel::onDismissError
            )

            ClienteDropdown(
                selectedCliente = uiState.selectedCliente,
                searchQuery = selectedClienteSearch,
                onSearchQueryChanged = { selectedClienteSearch = it; showClienteDropdown = true },
                filteredClientes = filteredClientes,
                showDropdown = showClienteDropdown,
                onDismissDropdown = { showClienteDropdown = false },
                onClienteSelected = { cliente ->
                    viewModel.onClienteSelected(cliente)
                    selectedClienteSearch = cliente.nombre
                    showClienteDropdown = false
                },
                onClear = {
                    viewModel.onClienteSelected(null)
                    selectedClienteSearch = ""
                }
            )

            ItemsSection(
                items = uiState.items,
                onRemoveItem = viewModel::onRemoverItem,
                onAddItem = { showAddItemDialog = true }
            )

            OutlinedTextField(
                value = uiState.notas,
                onValueChange = viewModel::onNotasChanged,
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = CurrencyUtils.formatCOP(uiState.items.sumOf { it.subtotal }),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Button(
                onClick = { viewModel.onRegistrar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = uiState.items.isNotEmpty() && !uiState.isLoading
            ) {
                Text("Registrar Venta")
            }
        }
    }

    LoadingOverlay(isLoading = uiState.isLoading)

    if (showAddItemDialog) {
        AddItemDialog(
            productos = uiState.productos,
            onDismiss = { showAddItemDialog = false },
            onAdd = { producto, cantidad, precio ->
                viewModel.onAgregarItem(producto, cantidad, precio)
                showAddItemDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClienteDropdown(
    selectedCliente: Cliente?,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    filteredClientes: List<Cliente>,
    showDropdown: Boolean,
    onDismissDropdown: () -> Unit,
    onClienteSelected: (Cliente) -> Unit,
    onClear: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = selectedCliente?.nombre ?: searchQuery,
            onValueChange = { onSearchQueryChanged(it) },
            label = { Text("Cliente (opcional)") },
            placeholder = { Text("Buscar cliente...") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = selectedCliente != null,
            trailingIcon = {
                if (selectedCliente != null) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Limpiar selección"
                        )
                    }
                }
            }
        )

        if (showDropdown && filteredClientes.isNotEmpty()) {
            androidx.compose.material3.DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = onDismissDropdown
            ) {
                filteredClientes.forEach { cliente ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = cliente.nombre,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (cliente.documento != null) {
                                    Text(
                                        text = cliente.documento,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        onClick = { onClienteSelected(cliente) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemsSection(
    items: List<ItemVentaForm>,
    onRemoveItem: (Int) -> Unit,
    onAddItem: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Productos",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        items.forEachIndexed { index, item ->
            ItemRow(item = item, onRemove = { onRemoveItem(index) })
        }

        TextButton(onClick = onAddItem) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("+ Agregar producto")
        }
    }
}

@Composable
private fun ItemRow(
    item: ItemVentaForm,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.producto.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.cantidad} x ${CurrencyUtils.formatCOP(item.precioUnitario)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = CurrencyUtils.formatCOP(item.subtotal),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Eliminar producto",
                    tint = Error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun AddItemDialog(
    productos: List<Producto>,
    onDismiss: () -> Unit,
    onAdd: (Producto, Int, Double) -> Unit
) {
    var selectedProducto by remember { mutableStateOf<Producto?>(null) }
    var cantidad by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProductos = remember(productos, searchQuery) {
        if (searchQuery.isBlank()) productos
        else productos.filter {
            it.nombre.contains(searchQuery, ignoreCase = true) ||
                    it.codigo.contains(searchQuery, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = selectedProducto?.nombre ?: searchQuery,
                    onValueChange = { searchQuery = it; selectedProducto = null },
                    label = { Text("Producto") },
                    placeholder = { Text("Buscar producto...") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = selectedProducto != null,
                    trailingIcon = {
                        if (selectedProducto != null) {
                            IconButton(onClick = { selectedProducto = null; searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Limpiar selección"
                                )
                            }
                        }
                    }
                )

                if (selectedProducto == null && filteredProductos.isNotEmpty()) {
                    Column(modifier = Modifier.height(200.dp)) {
                        filteredProductos.take(10).forEach { producto ->
                            TextButton(
                                onClick = {
                                    selectedProducto = producto
                                    precio = producto.precio.toString()
                                    searchQuery = producto.nombre
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                                    )
                                    Text(
                                        text = "${producto.codigo} - Stock: ${producto.cantidad}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = CurrencyUtils.formatCOP(producto.precio),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (selectedProducto != null) {
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it.filter { c -> c.isDigit() } },
                        label = { Text("Cantidad") },
                        placeholder = { Text("0") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { newVal ->
                            if (newVal.isEmpty() || newVal.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                                precio = newVal
                            }
                        },
                        label = { Text("Precio unitario") },
                        placeholder = { Text("0") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val prod = selectedProducto ?: return@TextButton
                    val qty = cantidad.toIntOrNull() ?: return@TextButton
                    val p = precio.toDoubleOrNull() ?: return@TextButton
                    if (qty > 0 && p > 0) {
                        onAdd(prod, qty, p)
                    }
                },
                enabled = selectedProducto != null && cantidad.toIntOrNull()?.let { it > 0 } == true &&
                        precio.toDoubleOrNull()?.let { it > 0 } == true
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
