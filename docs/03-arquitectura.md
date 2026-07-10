# Arquitectura — InventarioApp Android

---

## 1. Principios de Diseño

| Principio | Aplicación |
|-----------|-----------|
| **Separación de Capas** | Presentation → Domain → Data, sin referencias cruzadas hacia atrás |
| **Dependency Rule** | Domain no depende de Android Framework ni de ninguna librería externa |
| **Single Source of Truth** | Room como cache local, API como fuente remota, DataStore como preferencias |
| **Unidirectional Data Flow** | StateFlow de ViewModels → Composables, eventos de Composables → ViewModels |
| **Testability** | Cada capa se puede testear de forma aislada con mocks |
| **Offline-First** | Room emite datos cacheados primero, luego fetch remoto |

---

## 2. Diagrama de Capas (Clean Architecture)

```
┌─────────────────────────────────────────────────────────────────────┐
│                       PRESENTATION LAYER                           │
│                                                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │  Screen  │  │  Screen  │  │  Screen  │  │  Screen  │  ...      │
│  │(Compose) │  │(Compose) │  │(Compose) │  │(Compose) │           │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘           │
│       │              │             │              │                  │
│       ▼              ▼             ▼              ▼                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │ViewModel │  │ViewModel │  │ViewModel │  │ViewModel │           │
│  │  @HiltVM │  │  @HiltVM │  │  @HiltVM │  │  @HiltVM │           │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘           │
│       │              │             │              │                  │
│  UiState(StateFlow)  │             │              │                  │
│  UI Events           │             │              │                  │
├───────┼──────────────┼─────────────┼──────────────┼─────────────────┤
│       │              │             │    DOMAIN LAYER               │
│       │         ┌────┴─────────────┴──────────────┴────┐           │
│       │         │            USE CASES                   │           │
│       │         │  LoginUseCase                          │           │
│       │         │  GetProductosUseCase                   │           │
│       │         │  RegistrarMovimientoUseCase            │           │
│       │         │  CreateVentaUseCase                    │           │
│       │         │  ...                                   │           │
│       │         └──────────────────┬────────────────────┘           │
│       │                            │                                │
│       │         ┌──────────────────┴────────────────────┐           │
│       │         │     REPOSITORY INTERFACES              │           │
│       │         │  AuthRepository                        │           │
│       │         │  ProductoRepository                    │           │
│       │         │  MovimientoRepository                  │           │
│       │         │  VentaRepository                       │           │
│       │         │  ...                                   │           │
│       │         └──────────────────┬────────────────────┘           │
│       │                            │                                │
│  ┌────┴────────────────────────────┴──────────────────────┐        │
│  │                    DOMAIN MODELS                        │        │
│  │  Producto, Categoria, Movimiento, Venta, Cliente, ...   │        │
│  │  (pure Kotlin, no Android dependencies)                 │        │
│  └────────────────────────────────────────────────────────┘        │
├─────────────────────────────────────────────────────────────────────┤
│                          DATA LAYER                                │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                  REPOSITORY IMPL                              │  │
│  │  AuthRepositoryImpl  │  ProductoRepositoryImpl  │  ...       │  │
│  └───────┬──────────────┬──────────────────────────┬────────────┘  │
│          │              │                          │                │
│  ┌───────┴───────┐  ┌──┴───────────┐  ┌──────────┴──────────┐    │
│  │    REMOTE      │  │    LOCAL     │  │    PREFERENCES      │    │
│  │  Ktor Client   │  │   Room DB    │  │    DataStore        │    │
│  │  ┌──────────┐  │  │  ┌────────┐  │  │  ┌──────────────┐   │    │
│  │  │AuthApi   │  │  │  │ProdDao │  │  │  │AuthToken     │   │    │
│  │  │ProductoA │  │  │  │CategDao│  │  │  │Workspace     │   │    │
│  │  │CategoriaA│  │  │  │MovDao  │  │  │  │Theme         │   │    │
│  │  │Movimiento│  │  │  └────────┘  │  │  └──────────────┘   │    │
│  │  │VentaApi  │  │  │              │  │                      │    │
│  │  │ClienteAp │  │  │              │  │                      │    │
│  │  └──────────┘  │  │              │  │                      │    │
│  └───────┬───────┘  └──────────────┘  └──────────────────────┘    │
│          │                                                         │
├──────────┼─────────────────────────────────────────────────────────┤
│          │            EXTERNAL SERVICES                            │
│          ▼                                                         │
│  ┌───────────────────┐  ┌──────────────┐  ┌────────────────┐     │
│  │   Neon Auth       │  │  Next.js API │  │  Firebase      │     │
│  │   (Bearer Token)  │  │  (Vercel)    │  │  (Crashlytics) │     │
│  └───────────────────┘  └──────────────┘  └────────────────┘     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Estructura de Paquetes

```
com.inventario.app/
├── InventarioApp.kt                    # @HiltAndroidApp
│
├── di/                                 # Hilt Modules
│   ├── NetworkModule.kt                # Ktor Client + AuthInterceptor
│   ├── DatabaseModule.kt               # Room DB + DAOs
│   ├── RepositoryModule.kt             # Binds repository interfaces
│   ├── DataStoreModule.kt              # Auth + Workspace + Settings DataStore
│   └── FirebaseModule.kt              # Crashlytics + Analytics
│
├── domain/
│   ├── model/                          # Pure Kotlin models
│   │   ├── Organization.kt
│   │   ├── Workspace.kt
│   │   ├── Usuario.kt
│   │   ├── Producto.kt
│   │   ├── Categoria.kt
│   │   ├── Movimiento.kt
│   │   ├── TipoMovimiento.kt           # enum: entrada, salida
│   │   ├── Venta.kt
│   │   ├── ItemVenta.kt
│   │   ├── EstadoVenta.kt             # enum: PENDIENTE, COMPLETADA, CANCELADA
│   │   ├── Cliente.kt
│   │   ├── HistorialPrecio.kt
│   │   ├── DashboardMetrics.kt
│   │   └── Sesion.kt
│   │
│   ├── repository/                     # Interfaces only
│   │   ├── AuthRepository.kt
│   │   ├── OrganizationRepository.kt
│   │   ├── WorkspaceRepository.kt
│   │   ├── ProductoRepository.kt
│   │   ├── CategoriaRepository.kt
│   │   ├── MovimientoRepository.kt
│   │   ├── VentaRepository.kt
│   │   ├── ClienteRepository.kt
│   │   └── DashboardRepository.kt
│   │
│   ├── usecase/
│   │   ├── auth/
│   │   │   ├── LoginUseCase.kt
│   │   │   ├── GetSessionUseCase.kt
│   │   │   ├── GetOrganizationsUseCase.kt
│   │   │   ├── GetWorkspacesUseCase.kt
│   │   │   ├── SelectWorkspaceUseCase.kt
│   │   │   └── LogoutUseCase.kt
│   │   ├── producto/
│   │   │   ├── GetProductosUseCase.kt
│   │   │   ├── GetProductoByIdUseCase.kt
│   │   │   ├── CreateProductoUseCase.kt
│   │   │   ├── UpdateProductoUseCase.kt
│   │   │   └── DeleteProductoUseCase.kt
│   │   ├── categoria/
│   │   │   ├── GetCategoriasUseCase.kt
│   │   │   ├── CreateCategoriaUseCase.kt
│   │   │   └── UpdateCategoriaUseCase.kt
│   │   ├── movimiento/
│   │   │   ├── GetMovimientosUseCase.kt
│   │   │   └── RegistrarMovimientoUseCase.kt
│   │   ├── venta/
│   │   │   ├── GetVentasUseCase.kt
│   │   │   ├── GetVentaByIdUseCase.kt
│   │   │   ├── CreateVentaUseCase.kt
│   │   │   └── CancelarVentaUseCase.kt
│   │   ├── cliente/
│   │   │   ├── GetClientesUseCase.kt
│   │   │   ├── GetClienteByIdUseCase.kt
│   │   │   ├── CreateClienteUseCase.kt
│   │   │   └── UpdateClienteUseCase.kt
│   │   └── dashboard/
│   │       └── GetDashboardUseCase.kt
│   │
│   └── error/
│       ├── DomainError.kt             # sealed class
│       └── Result.kt                  # Result<T> wrapper
│
├── data/
│   ├── remote/
│   │   ├── api/                       # Ktor endpoint definitions
│   │   │   ├── AuthApi.kt
│   │   │   ├── OrganizationApi.kt
│   │   │   ├── WorkspaceApi.kt
│   │   │   ├── ProductoApi.kt
│   │   │   ├── CategoriaApi.kt
│   │   │   ├── MovimientoApi.kt
│   │   │   ├── VentaApi.kt
│   │   │   ├── ClienteApi.kt
│   │   │   └── DashboardApi.kt
│   │   ├── dto/                       # API response/request DTOs
│   │   │   ├── AuthDtos.kt
│   │   │   ├── OrganizationDto.kt
│   │   │   ├── WorkspaceDto.kt
│   │   │   ├── ProductoDto.kt
│   │   │   ├── CategoriaDto.kt
│   │   │   ├── MovimientoDto.kt
│   │   │   ├── VentaDto.kt
│   │   │   ├── ClienteDto.kt
│   │   │   └── DashboardDto.kt
│   │   ├── interceptor/
│   │   │   └── AuthInterceptor.kt     # Adds Bearer token to requests
│   │   └── KtorClientFactory.kt
│   │
│   ├── local/
│   │   ├── db/
│   │   │   ├── InventarioDatabase.kt
│   │   │   ├── Converters.kt          # Room type converters
│   │   │   ├── dao/
│   │   │   │   ├── ProductoDao.kt
│   │   │   │   ├── CategoriaDao.kt
│   │   │   │   └── MovimientoDao.kt
│   │   │   └── entity/
│   │   │       ├── ProductoEntity.kt
│   │   │       ├── CategoriaEntity.kt
│   │   │       └── MovimientoEntity.kt
│   │   └── datastore/
│   │       ├── AuthDataStore.kt       # Token, user session
│   │       ├── WorkspaceDataStore.kt  # orgSlug, wsSlug, wsId
│   │       └── SettingsDataStore.kt   # theme, language
│   │
│   ├── repository/                    # Implements domain interfaces
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── OrganizationRepositoryImpl.kt
│   │   ├── WorkspaceRepositoryImpl.kt
│   │   ├── ProductoRepositoryImpl.kt
│   │   ├── CategoriaRepositoryImpl.kt
│   │   ├── MovimientoRepositoryImpl.kt
│   │   ├── VentaRepositoryImpl.kt
│   │   ├── ClienteRepositoryImpl.kt
│   │   └── DashboardRepositoryImpl.kt
│   │
│   └── mapper/                        # DTO <-> Domain <-> Entity
│       ├── ProductoMapper.kt
│       ├── CategoriaMapper.kt
│       ├── MovimientoMapper.kt
│       ├── VentaMapper.kt
│       ├── ClienteMapper.kt
│       └── OrganizationMapper.kt
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── navigation/
│   │   ├── AppNavigation.kt           # NavHost + graph
│   │   ├── Screen.kt                  # Sealed class of routes
│   │   └── NavArgs.kt                 # Type-safe arguments
│   ├── components/                     # Reusable composables
│   │   ├── InventarioTopBar.kt
│   │   ├── EmptyState.kt
│   │   ├── LoadingOverlay.kt
│   │   ├── ErrorBanner.kt
│   │   ├── ConfirmDeleteDialog.kt
│   │   ├── StockBadge.kt
│   │   ├── MetricCard.kt
│   │   ├── SearchBar.kt
│   │   └── FilterChip.kt
│   ├── auth/
│   │   ├── LoginScreen.kt
│   │   └── LoginViewModel.kt
│   ├── onboarding/
│   │   ├── OrganizationSelectorScreen.kt
│   │   ├── OrganizationSelectorViewModel.kt
│   │   ├── WorkspaceSelectorScreen.kt
│   │   └── WorkspaceSelectorViewModel.kt
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── producto/
│   │   ├── ProductosScreen.kt
│   │   ├── ProductoFormScreen.kt
│   │   └── ProductoViewModel.kt
│   ├── categoria/
│   │   ├── CategoriasScreen.kt
│   │   └── CategoriaViewModel.kt
│   ├── movimiento/
│   │   ├── MovimientosScreen.kt
│   │   ├── MovimientoFormScreen.kt
│   │   └── MovimientoViewModel.kt
│   ├── venta/
│   │   ├── VentasScreen.kt
│   │   ├── VentaFormScreen.kt
│   │   └── VentaViewModel.kt
│   ├── cliente/
│   │   ├── ClientesScreen.kt
│   │   ├── ClienteFormScreen.kt
│   │   └── ClienteViewModel.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       └── SettingsViewModel.kt
│
└── core/
    ├── util/
    │   ├── DateUtils.kt               # Format dates for display
    │   ├── CurrencyUtils.kt           # COP formatting
    │   └── ValidationUtils.kt         # Form validation
    ├── extension/
    │   ├── FlowExtensions.kt          # Result<T> transforms
    │   └── StringExtensions.kt
    └── di/
        └── Qualifiers.kt              # Hilt qualifiers
```

---

## 4. Dependencias del Version Catalog

```toml
[versions]
agp = "9.2.0"
kotlin = "2.1.0"
hilt = "2.53"
ktor = "3.1.0"
room = "2.7.1"
datastore = "1.1.7"
navigation = "2.8.5"
coil = "3.1.0"
paging = "3.3.6"
mockk = "1.14.2"
turbine = "1.3.0"
junit5 = "5.11.4"

[libraries]
# Hilt
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hilt" }
hilt-compiler = { module = "com.google.dagger:hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version = "1.2.0" }

# Ktor Client
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }

# Room
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }

# DataStore
datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }

# Paging
paging-runtime = { module = "androidx.paging:paging-runtime", version.ref = "paging" }
paging-compose = { module = "androidx.paging:paging-compose", version.ref = "paging" }

# Testing
junit5 = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit5" }
mockk = { module = "io.mockk:mockk", version.ref = "mockk" }
turbine = { module = "app.cash.turbine:turbine", version.ref = "turbine" }

[plugins]
kotlin-android = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "2.1.0-1.0.29" }
```

---

## 5. Patrones de Código

### 5.1 ViewModel Pattern

```kotlin
@HiltViewModel
class ProductosViewModel @Inject constructor(
    private val getProductos: GetProductosUseCase,
    private val createProducto: CreateProductoUseCase,
    private val deleteProducto: DeleteProductoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductosUiState())
    val uiState: StateFlow<ProductosUiState> = _uiState.asStateFlow()

    init {
        loadProductos()
    }

    private fun loadProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getProductos()
                .onSuccess { productos ->
                    _uiState.update { it.copy(isLoading = false, productos = productos) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
```

### 5.2 Repository Pattern (Offline-First)

```kotlin
class ProductoRepositoryImpl @Inject constructor(
    private val api: ProductoApi,
    private val dao: ProductoDao,
    private val mapper: ProductoMapper
) : ProductoRepository {

    override fun getProductos(): Flow<List<Producto>> = flow {
        // 1. Emit cached data first (fast)
        val cached = dao.getAll().map { mapper.toDomain(it) }
        emit(cached)

        // 2. Fetch from network (background)
        try {
            val remote = api.getProductos()
            // 3. Update Room cache
            dao.upsertAll(remote.map { mapper.toEntity(it) })
            // 4. Emit updated data
            emit(dao.getAll().map { mapper.toDomain(it) })
        } catch (e: Exception) {
            // Cache already emitted, user sees cached data
            // Error is logged but not fatal for reads
        }
    }
}
```

### 5.3 UseCase Pattern

```kotlin
class RegistrarMovimientoUseCase @Inject constructor(
    private val repository: MovimientoRepository,
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(
        productoId: Int,
        tipo: TipoMovimiento,
        cantidad: Int,
        notas: String?
    ): Result<Movimiento> {
        // Validate business rules
        if (cantidad <= 0) {
            return Result.failure(DomainError.Validation("cantidad", "Debe ser mayor a 0"))
        }

        if (tipo == TipoMovimiento.salida) {
            val producto = productoRepository.getProductoById(productoId)
                .getOrNull()
                ?: return Result.failure(DomainError.NotFound("Producto no encontrado"))

            if (producto.cantidad < cantidad) {
                return Result.failure(
                    DomainError.Validation("cantidad", "Stock insuficiente (${producto.cantidad} disponible)")
                )
            }
        }

        return repository.registrarMovimiento(productoId, tipo, cantidad, notas)
    }
}
```

### 5.4 Error Handling

```kotlin
sealed class DomainError(val message: String) {
    object NetworkError : DomainError("Sin conexion a internet")
    object Unauthorized : DomainError("Sesion expirada")
    object Forbidden : DomainError("No tienes permisos para esta accion")
    data class NotFound(val msg: String) : DomainError(msg)
    data class ServerError(val code: Int) : DomainError("Error del servidor ($code)")
    data class Validation(val field: String, val msg: String) : DomainError(msg)
}

// Usage in ViewModels
viewModelScope.launch {
    resultado
        .onSuccess { movimiento ->
            _uiState.update { it.copy(showSuccess = true) }
        }
        .onFailure { error ->
            when (error) {
                is DomainError.Unauthorized -> navigateToLogin()
                is DomainError.Validation -> _uiState.update { it.copy(fieldError = error.msg) }
                else -> _uiState.update { it.copy(error = error.message) }
            }
        }
}
```
