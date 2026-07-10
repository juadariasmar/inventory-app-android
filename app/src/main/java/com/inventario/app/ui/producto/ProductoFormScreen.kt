package com.inventario.app.ui.producto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.domain.model.Categoria
import com.inventario.app.ui.components.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ProductoFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val savedSuccessfully by viewModel.savedSuccessfully.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showBackDialog by remember { mutableStateOf(false) }
    var hasUnsavedChanges by remember { mutableStateOf(false) }

    LaunchedEffect(savedSuccessfully) {
        if (savedSuccessfully) {
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onDismissError()
        }
    }

    LaunchedEffect(uiState.nombre, uiState.codigo, uiState.precio, uiState.categoriaId) {
        hasUnsavedChanges = uiState.nombre.isNotBlank() ||
                uiState.codigo.isNotBlank() ||
                uiState.precio.isNotBlank() ||
                uiState.categoriaId != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.isEditing) "Editar Producto" else "Nuevo Producto",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasUnsavedChanges) {
                            showBackDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::onSave,
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Guardar"
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FormTextField(
                    value = uiState.codigo,
                    onValueChange = viewModel::onCodigoChanged,
                    label = "Código *",
                    error = uiState.fieldErrors["codigo"],
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                FormTextField(
                    value = uiState.nombre,
                    onValueChange = viewModel::onNombreChanged,
                    label = "Nombre *",
                    error = uiState.fieldErrors["nombre"],
                    enabled = !uiState.isLoading
                )

                FormTextField(
                    value = uiState.descripcion,
                    onValueChange = viewModel::onDescripcionChanged,
                    label = "Descripción",
                    enabled = !uiState.isLoading,
                    singleLine = false
                )

                CategoriaDropdown(
                    categorias = categorias,
                    selectedId = uiState.categoriaId,
                    onSelected = viewModel::onCategoriaSelected,
                    error = uiState.fieldErrors["categoriaId"],
                    enabled = !uiState.isLoading
                )

                FormTextField(
                    value = uiState.precio,
                    onValueChange = viewModel::onPrecioChanged,
                    label = "Precio *",
                    error = uiState.fieldErrors["precio"],
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = "$"
                )

                FormTextField(
                    value = uiState.stockMinimo,
                    onValueChange = viewModel::onStockMinimoChanged,
                    label = "Stock Mínimo",
                    error = uiState.fieldErrors["stockMinimo"],
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = viewModel::onSave,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = if (viewModel.isEditing) "Actualizar" else "Crear Producto",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (uiState.isLoading) {
                LoadingOverlay(isLoading = true)
            }
        }
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = { showBackDialog = false },
            title = { Text("Cambios sin guardar") },
            text = { Text("¿Deseas salir sin guardar los cambios?") },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog = false
                    onNavigateBack()
                }) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    prefix: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = label },
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        },
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        prefix = prefix?.let { { Text(it) } },
        shape = MaterialTheme.shapes.medium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriaDropdown(
    categorias: List<Categoria>,
    selectedId: Int?,
    onSelected: (Int) -> Unit,
    error: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategoria = categorias.find { it.id == selectedId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it }
    ) {
        OutlinedTextField(
            value = selectedCategoria?.nombre ?: "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .semantics { contentDescription = "Seleccionar categoría" },
            label = { Text("Categoría *") },
            readOnly = true,
            isError = error != null,
            supportingText = error?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            enabled = enabled,
            shape = MaterialTheme.shapes.medium
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nombre) },
                    onClick = {
                        onSelected(categoria.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
