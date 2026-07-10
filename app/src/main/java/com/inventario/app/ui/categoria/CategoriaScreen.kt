package com.inventario.app.ui.categoria

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.domain.model.Categoria
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: CategoriaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formUiState by viewModel.formUiState.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val createdSuccessfully by viewModel.createdSuccessfully.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(createdSuccessfully) {
        if (createdSuccessfully) {
            snackbarHostState.showSnackbar("Categoría creada exitosamente")
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onDismissError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onShowDialog) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar categoría"
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
                onClick = viewModel::onShowDialog,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Crear categoría"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingOverlay(isLoading = true)
                }
                uiState.categorias.isEmpty() -> {
                    EmptyState(
                        icon = Icons.Filled.Category,
                        title = "No hay categorías",
                        description = "Crea tu primera categoría para organizar tus productos.",
                        actionText = "Crear categoría",
                        onAction = viewModel::onShowDialog
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.categorias, key = { it.id }) { categoria ->
                            CategoriaItem(
                                categoria = categoria,
                                productoCount = uiState.productoCountByCategoria[categoria.id] ?: 0
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddCategoriaDialog(
            nombre = formUiState.nombre,
            prefijo = formUiState.prefijo,
            fieldErrors = formUiState.fieldErrors,
            isLoading = formUiState.isLoading,
            error = formUiState.error,
            onNombreChanged = viewModel::onFormNombreChanged,
            onPrefijoChanged = viewModel::onFormPrefijoChanged,
            onConfirm = viewModel::onCreateCategoria,
            onDismiss = viewModel::onDismissDialog,
            onDismissError = { /* handled by form state */ }
        )
    }
}

@Composable
private fun CategoriaItem(
    categoria: Categoria,
    productoCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "${categoria.nombre}, prefijo ${categoria.prefijo}, $productoCount productos"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (productoCount == 1) "1 producto" else "$productoCount productos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = categoria.prefijo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .semantics { contentDescription = "Prefijo: ${categoria.prefijo}" }
            )
        }
    }
}

@Composable
private fun AddCategoriaDialog(
    nombre: String,
    prefijo: String,
    fieldErrors: Map<String, String>,
    isLoading: Boolean,
    error: String?,
    onNombreChanged: (String) -> Unit,
    onPrefijoChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDismissError: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = "Nueva Categoría",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = onNombreChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Nombre de la categoría" },
                    label = { Text("Nombre *") },
                    isError = fieldErrors["nombre"] != null,
                    supportingText = fieldErrors["nombre"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = !isLoading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                OutlinedTextField(
                    value = prefijo,
                    onValueChange = onPrefijoChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Prefijo de la categoría" },
                    label = { Text("Prefijo *") },
                    isError = fieldErrors["prefijo"] != null,
                    supportingText = fieldErrors["prefijo"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = !isLoading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                error?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading && nombre.isNotBlank() && prefijo.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    )
}
