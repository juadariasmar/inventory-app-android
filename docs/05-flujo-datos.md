# Flujo de Datos — InventarioApp Android

---

## 1. Estrategia: Offline-First con Sync

```
┌─────────────────────────────────────────────────────────────────┐
│                    DATA FLOW STRATEGY                           │
│                                                                 │
│  ┌─────────────┐    ┌──────────────┐    ┌──────────────┐       │
│  │   REMOTE    │    │   DOMAIN     │    │   LOCAL      │       │
│  │  (Ktor)     │───▶│  (UseCase)   │◀───│  (Room)      │       │
│  └─────────────┘    └──────┬───────┘    └──────────────┘       │
│                            │                                    │
│                     ┌──────▼───────┐                            │
│                     │  DATASTORE   │                            │
│                     │  (Prefs)     │                            │
│                     └──────────────┘                            │
│                                                                 │
│  Lectura:  Room (cached) → emit → Fetch remoto → update Room   │
│  Escritura: POST/API → on success → update Room cache          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Flujo de Lectura (Productos)

```
 Screen              ViewModel             Repository            API
   │                    │                     │                   │
   │ 1.loadProductos()  │                     │                   │
   │───────────────────>│                     │                   │
   │                    │ 2.getProductos()    │                   │
   │                    │────────────────────>│                   │
   │                    │                     │                   │
   │                    │    ┌────────────────┤                   │
   │                    │    │ 3a. Room cache? │                   │
   │                    │    │ YES → emit      │                   │
   │                    │    └────────────────┤                   │
   │                    │                     │                   │
   │                    │                     │ 4.GET /productos  │
   │                    │                     │  Bearer: <token>  │
   │                    │                     │──────────────────>│
   │                    │                     │                   │
   │                    │                     │ 5.{productos[]}   │
   │                    │                     │<──────────────────│
   │                    │                     │                   │
   │                    │                     │ 6.upsert Room     │
   │                    │                     │────┐              │
   │                    │                     │    │ Room DB      │
   │                    │                     │<───┘              │
   │                    │                     │                   │
   │                    │ 7.Flow<Productos>   │                   │
   │                    │<────────────────────│                   │
   │                    │                     │                   │
   │ 8.UI Update        │                     │                   │
   │<───────────────────│                     │                   │
```

**Implementacion:**

```kotlin
// Repository
override fun getProductos(): Flow<List<Producto>> = flow {
    val cached = dao.getAll().map { mapper.toDomain(it) }
    emit(cached)

    try {
        val remote = api.getProductos(orgSlug, wsSlug)
        dao.upsertAll(remote.map { mapper.toEntity(it) })
        emit(dao.getAll().map { mapper.toDomain(it) })
    } catch (e: Exception) {
        // Cache already emitted, user sees cached data
    }
}

// ViewModel
init {
    viewModelScope.launch {
        getProductos().collect { productos ->
            _uiState.update { it.copy(isLoading = false, productos = productos) }
        }
    }
}
```

---

## 3. Flujo de Escritura (Registrar Movimiento)

```
 Screen              ViewModel             Repository            API
   │                    │                     │                   │
   │ 1.onRegistrar()    │                     │                   │
   │───────────────────>│                     │                   │
   │                    │ 2.registrarMov()    │                   │
   │                    │────────────────────>│                   │
   │                    │                     │                   │
   │                    │                     │ 3.POST /movim.    │
   │                    │                     │  Bearer: <token>  │
   │                    │                     │──────────────────>│
   │                    │                     │                   │
   │                    │                     │ 4.{movimiento}    │
   │                    │                     │<──────────────────│
   │                    │                     │                   │
   │                    │                     │ 5.updateRoom()    │
   │                    │                     │────┐              │
   │                    │                     │    │ Room DB      │
   │                    │                     │<───┘              │
   │                    │                     │                   │
   │                    │ 6.Result.Success    │                   │
   │                    │<────────────────────│                   │
   │                    │                     │                   │
   │ 7.Snackbar +       │                     │                   │
   │   navigateBack     │                     │                   │
   │<───────────────────│                     │                   │
```

**Implementacion:**

```kotlin
// UseCase
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
        if (cantidad <= 0) {
            return Result.failure(DomainError.Validation("cantidad", "Debe ser mayor a 0"))
        }

        if (tipo == TipoMovimiento.salida) {
            val producto = productoRepository.getProductoById(productoId).getOrNull()
                ?: return Result.failure(DomainError.NotFound("Producto no encontrado"))

            if (producto.cantidad < cantidad) {
                return Result.failure(
                    DomainError.Validation("cantidad",
                        "Stock insuficiente (${producto.cantidad} disponible)")
                )
            }
        }

        return repository.registrarMovimiento(productoId, tipo, cantidad, notas)
    }
}

// Repository
override suspend fun registrarMovimiento(
    productoId: Int,
    tipo: TipoMovimiento,
    cantidad: Int,
    notas: String?
): Result<Movimiento> = runCatching {
    val dto = api.registrarMovimiento(orgSlug, wsSlug, MovimientoRequest(
        productoId = productoId,
        tipo = tipo.name.lowercase(),
        cantidad = cantidad,
        notas = notas
    ))
    // Update local cache
    dao.incrementStock(
        productoId = productoId,
        delta = if (tipo == TipoMovimiento.entrada) cantidad else -cantidad
    )
    mapper.toDomain(dto)
}
```

---

## 4. Token Lifecycle

```
┌──────────────────────────────────────────────────────────────┐
│                    TOKEN LIFECYCLE                            │
│                                                              │
│  1. LOGIN                                                    │
│     POST /api/auth/sign-in/email                             │
│     → Response header: set-auth-token                        │
│     → Save to EncryptedSharedPreferences                     │
│                                                              │
│  2. EVERY REQUEST                                            │
│     AuthInterceptor reads token from DataStore               │
│     → Adds header: Authorization: Bearer <token>             │
│                                                              │
│  3. TOKEN VALIDATION                                         │
│     GET /api/auth/get-session                                │
│     → If valid: continue                                     │
│     → If 401: token expired                                  │
│       → Clear token                                          │
│       → Navigate to Login                                    │
│                                                              │
│  4. LOGOUT                                                   │
│     POST /api/auth/sign-out                                  │
│     → Clear token from DataStore                             │
│     → Navigate to Login (popUpTo root)                       │
│                                                              │
│  Storage:                                                    │
│  - EncryptedSharedPreferences (Android Keystore)             │
│  - Key: "auth_token"                                         │
│  - Cleared on logout or token expiry                         │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

**Implementacion:**

```kotlin
// AuthInterceptor
class AuthInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore
) : HttpInterceptor {
    override suspend fun intercept(chain: HttpInterceptor.Chain): HttpResponse {
        val token = authDataStore.getToken()
        val request = if (token != null) {
            chain.call.request.build {
                headers.append("Authorization", "Bearer $token")
            }
        } else {
            chain.call.request
        }
        return chain.proceed(request)
    }
}

// AuthRepository
override suspend fun login(email: String, password: String): Result<Sesion> = runCatching {
    val response = api.signIn(SignInRequest(email, password))
    authDataStore.saveToken(response.token)
    Sesion(
        userId = response.user.id,
        email = response.user.email,
        nombre = response.user.name
    )
}

override suspend fun logout() {
    try { api.signOut() } catch (_: Exception) {}
    authDataStore.clearToken()
    workspaceDataStore.clear()
}
```

---

## 5. Workspace Context Management

```
┌──────────────────────────────────────────────────────────────┐
│           WORKSPACE CONTEXT MANAGEMENT                       │
│                                                              │
│  After login + org/ws selection:                             │
│                                                              │
│  DataStore saves:                                            │
│  - orgSlug: "mi-empresa"                                     │
│  - wsSlug: "bodega-principal"                                │
│  - wsId: "clxyz123..."                                       │
│  - wsNombre: "Bodega Principal"                              │
│                                                              │
│  Every API call uses:                                        │
│  GET  /api/{orgSlug}/{wsSlug}/productos                      │
│  POST /api/{orgSlug}/{wsSlug}/movimientos                    │
│  GET  /api/{orgSlug}/{wsSlash}/ventas                        │
│                                                              │
│  If workspace changes:                                       │
│  - Update DataStore                                          │
│  - Clear Room cache                                          │
│  - Refetch all data                                          │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

**Implementacion:**

```kotlin
// DataStore
class WorkspaceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val orgSlug: Flow<String> = dataStore.data.map { it[stringPreferencesKey("org_slug")] ?: "" }
    val wsSlug: Flow<String> = dataStore.data.map { it[stringPreferencesKey("ws_slug")] ?: "" }
    val wsId: Flow<String> = dataStore.data.map { it[stringPreferencesKey("ws_id")] ?: "" }
    val wsNombre: Flow<String> = dataStore.data.map { it[stringPreferencesKey("ws_nombre")] ?: "" }

    suspend fun selectWorkspace(org: Organization, workspace: Workspace) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("org_slug")] = org.slug
            prefs[stringPreferencesKey("ws_slug")] = workspace.slug
            prefs[stringPreferencesKey("ws_id")] = workspace.id
            prefs[stringPreferencesKey("ws_nombre")] = workspace.nombre
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}

// Ktor Client setup
@Provides
@Singleton
fun provideKtorClient(
    authInterceptor: AuthInterceptor,
    workspaceDataStore: WorkspaceDataStore
): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; isLenient = true })
    }
    install(Logging) { level = LogLevel.BODY }
    defaultRequest {
        // Base URL from BuildConfig
        url("https://inventory-project-ten.vercel.app/api/")
    }
    install(AuthInterceptor) // custom plugin
}
```
