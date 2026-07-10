# Modelo de Datos — InventarioApp Android

---

## 1. Domain Models (mirror de Prisma)

```kotlin
// ═══════════════════════════════════════════════════════════════
// AUTH & USER
// ═══════════════════════════════════════════════════════════════

data class Sesion(
    val userId: String,        // internal DB id (NOT neonAuthId)
    val email: String,
    val nombre: String,
    val rol: String,           // SUPER_ADMIN | ADMIN | USUARIO
    val estado: String,        // ACTIVO | SUSPENDIDO
    val permisos: List<String> // VER_ANALISIS, REGISTRAR_MOVIMIENTOS, etc.
)

data class Organization(
    val id: String,
    val nombre: String,
    val slug: String,
    val planStatus: String     // FREE | PRO | ENTERPRISE
)

data class Workspace(
    val id: String,
    val nombre: String,
    val slug: String,
    val organizationId: String
)

// ═══════════════════════════════════════════════════════════════
// INVENTORY
// ═══════════════════════════════════════════════════════════════

data class Categoria(
    val id: Int,
    val nombre: String,
    val prefijo: String
)

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoriaNombre: String?,  // joined from Categoria
    val creadoEn: String
)

enum class TipoMovimiento { entrada, salida }

data class Movimiento(
    val id: Int,
    val productoId: Int,
    val productoNombre: String?,   // joined from Producto
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val notas: String?,
    val ventaId: Int?,
    val ordenCompraId: Int?,
    val creadoEn: String,
    val usuarioNombre: String?     // joined from Usuario
)

data class HistorialPrecio(
    val id: Int,
    val productoId: Int,
    val precioAnterior: Double,
    val precioNuevo: Double,
    val cambiadoPorNombre: String?,
    val creadoEn: String
)

// ═══════════════════════════════════════════════════════════════
// SALES
// ═══════════════════════════════════════════════════════════════

enum class EstadoVenta { PENDIENTE, COMPLETADA, CANCELADA }

data class Venta(
    val id: Int,
    val vendedorNombre: String?,
    val clienteNombre: String?,
    val total: Double,
    val notas: String?,
    val items: List<ItemVenta>,
    val canceladaEn: String?,
    val motivoCancelacion: String?,
    val creadoEn: String
)

data class ItemVenta(
    val id: Int,
    val productoId: Int,
    val productoNombre: String?,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)

// ═══════════════════════════════════════════════════════════════
// CLIENTS
// ═══════════════════════════════════════════════════════════════

data class Cliente(
    val id: Int,
    val nombre: String,
    val documento: String?,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val notas: String?
)

// ═══════════════════════════════════════════════════════════════
// DASHBOARD
// ═══════════════════════════════════════════════════════════════

data class DashboardMetrics(
    val totalProductos: Int,
    val totalUnidades: Int,
    val stockBajo: Int,
    val valorInventario: Double,
    val ultimosMovimientos: List<Movimiento>
)

// ═══════════════════════════════════════════════════════════════
// UI STATE
// ═══════════════════════════════════════════════════════════════

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class OrganizationSelectorUiState(
    val isLoading: Boolean = false,
    val organizations: List<Organization> = emptyList(),
    val error: String? = null
)

data class WorkspaceSelectorUiState(
    val isLoading: Boolean = false,
    val workspaces: List<Workspace> = emptyList(),
    val error: String? = null
)

data class DashboardUiState(
    val isLoading: Boolean = false,
    val metrics: DashboardMetrics? = null,
    val error: String? = null
)

data class ProductosUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)

data class MovimientosUiState(
    val isLoading: Boolean = false,
    val movimientos: List<Movimiento> = emptyList(),
    val error: String? = null,
    val filtroTipo: TipoMovimiento? = null
)

data class MovimientoFormUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val selectedProducto: Producto? = null,
    val tipo: TipoMovimiento = TipoMovimiento.entrada,
    val cantidad: String = "",
    val notas: String = "",
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

data class VentasUiState(
    val isLoading: Boolean = false,
    val ventas: List<Venta> = emptyList(),
    val error: String? = null
)

data class VentaFormUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val selectedCliente: Cliente? = null,
    val items: List<ItemVentaForm> = emptyList(),
    val notas: String = "",
    val error: String? = null
)

data class ItemVentaForm(
    val producto: Producto,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}

data class ClientesUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)
```

---

## 2. DTOs (API Response Mapping)

```kotlin
// ═══════════════════════════════════════════════════════════════
// AUTH DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignInResponse(
    val redirect: Boolean,
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String
)

@Serializable
data class SessionResponse(
    val session: SessionDto?,
    val user: UserDto?
)

@Serializable
data class SessionDto(
    val userId: String,
    val expiresAt: String
)

// ═══════════════════════════════════════════════════════════════
// ORGANIZATION & WORKSPACE DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class OrganizationDto(
    val id: String,
    val nombre: String,
    val slug: String,
    val planStatus: String
) {
    fun toDomain() = Organization(id, nombre, slug, planStatus)
}

@Serializable
data class WorkspaceDto(
    val id: String,
    val nombre: String,
    val slug: String,
    val organizationId: String
) {
    fun toDomain() = Workspace(id, nombre, slug, organizationId)
}

// ═══════════════════════════════════════════════════════════════
// PRODUCTO DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoria: CategoriaDto? = null,
    val creadoEn: String
) {
    fun toDomain() = Producto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        codigo = codigo,
        precio = precio,
        cantidad = cantidad,
        stockMinimo = stockMinimo,
        categoriaId = categoriaId,
        categoriaNombre = categoria?.nombre,
        creadoEn = creadoEn
    )
}

@Serializable
data class ProductoRequest(
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val stockMinimo: Int,
    val categoriaId: Int
)

// ═══════════════════════════════════════════════════════════════
// CATEGORIA DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class CategoriaDto(
    val id: Int,
    val nombre: String,
    val prefijo: String
) {
    fun toDomain() = Categoria(id, nombre, prefijo)
}

@Serializable
data class CategoriaRequest(
    val nombre: String,
    val prefijo: String
)

// ═══════════════════════════════════════════════════════════════
// MOVIMIENTO DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class MovimientoDto(
    val id: Int,
    val productoId: Int,
    val producto: ProductoDto? = null,
    val tipo: String,
    val cantidad: Int,
    val notas: String?,
    val ventaId: Int?,
    val ordenCompraId: Int?,
    val creadoEn: String,
    val usuario: UserDto? = null
) {
    fun toDomain() = Movimiento(
        id = id,
        productoId = productoId,
        productoNombre = producto?.nombre,
        tipo = TipoMovimiento.valueOf(tipo),
        cantidad = cantidad,
        notas = notas,
        ventaId = ventaId,
        ordenCompraId = ordenCompraId,
        creadoEn = creadoEn,
        usuarioNombre = usuario?.name
    )
}

@Serializable
data class MovimientoRequest(
    val productoId: Int,
    val tipo: String,
    val cantidad: Int,
    val notas: String? = null
)

// ═══════════════════════════════════════════════════════════════
// VENTA DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class VentaDto(
    val id: Int,
    val vendedor: UserDto? = null,
    val cliente: ClienteDto? = null,
    val total: Double,
    val notas: String?,
    val items: List<ItemVentaDto>,
    val canceladaEn: String?,
    val motivoCancelacion: String?,
    val creadoEn: String
) {
    fun toDomain() = Venta(
        id = id,
        vendedorNombre = vendedor?.name,
        clienteNombre = cliente?.nombre,
        total = total,
        notas = notas,
        items = items.map { it.toDomain() },
        canceladaEn = canceladaEn,
        motivoCancelacion = motivoCancelacion,
        creadoEn = creadoEn
    )
}

@Serializable
data class ItemVentaDto(
    val id: Int,
    val productoId: Int,
    val producto: ProductoDto? = null,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
) {
    fun toDomain() = ItemVenta(
        id = id,
        productoId = productoId,
        productoNombre = producto?.nombre,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}

@Serializable
data class VentaRequest(
    val clienteId: Int?,
    val items: List<ItemVentaRequest>,
    val notas: String?
)

@Serializable
data class ItemVentaRequest(
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)

// ═══════════════════════════════════════════════════════════════
// CLIENTE DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class ClienteDto(
    val id: Int,
    val nombre: String,
    val documento: String?,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val notas: String?
) {
    fun toDomain() = Cliente(id, nombre, documento, email, telefono, direccion, notas)
}

@Serializable
data class ClienteRequest(
    val nombre: String,
    val documento: String?,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val notas: String?
)

// ═══════════════════════════════════════════════════════════════
// DASHBOARD DTOs
// ═══════════════════════════════════════════════════════════════

@Serializable
data class DashboardDto(
    val totalProductos: Int,
    val totalUnidades: Int,
    val stockBajo: Int,
    val valorInventario: Double,
    val ultimosMovimientos: List<MovimientoDto>
) {
    fun toDomain() = DashboardMetrics(
        totalProductos = totalProductos,
        totalUnidades = totalUnidades,
        stockBajo = stockBajo,
        valorInventario = valorInventario,
        ultimosMovimientos = ultimosMovimientos.map { it.toDomain() }
    )
}
```

---

## 3. Room Entities (Local Cache)

```kotlin
// ═══════════════════════════════════════════════════════════════
// ROOM ENTITIES
// ═══════════════════════════════════════════════════════════════

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val prefijo: String,
    val workspaceId: String,
    val lastSynced: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "productos",
    foreignKeys = [ForeignKey(
        entity = CategoriaEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoriaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoriaId"), Index("workspaceId")]
)
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoriaNombre: String?,
    val workspaceId: String,
    val creadoEn: String,
    val lastSynced: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "movimientos",
    indices = [Index("workspaceId"), Index("productoId")]
)
data class MovimientoEntity(
    @PrimaryKey val id: Int,
    val productoId: Int,
    val productoNombre: String?,
    val tipo: String,
    val cantidad: Int,
    val notas: String?,
    val ventaId: Int?,
    val ordenCompraId: Int?,
    val workspaceId: String,
    val creadoEn: String,
    val usuarioNombre: String?,
    val lastSynced: Long = System.currentTimeMillis()
)

// ═══════════════════════════════════════════════════════════════
// ROOM DAOs
// ═══════════════════════════════════════════════════════════════

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE workspaceId = :wsId ORDER BY nombre ASC")
    fun getAll(wsId: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getById(id: Int): ProductoEntity?

    @Query("""
        SELECT * FROM productos
        WHERE workspaceId = :wsId
        AND (nombre LIKE '%' || :query || '%' OR codigo LIKE '%' || :query || '%')
        ORDER BY nombre ASC
    """)
    fun search(wsId: String, query: String): Flow<List<ProductoEntity>>

    @Upsert
    suspend fun upsertAll(productos: List<ProductoEntity>)

    @Query("UPDATE productos SET cantidad = cantidad + :delta WHERE id = :productoId")
    suspend fun adjustStock(productoId: Int, delta: Int)

    @Query("DELETE FROM productos WHERE workspaceId = :wsId")
    suspend fun deleteAll(wsId: String)
}

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias WHERE workspaceId = :wsId ORDER BY nombre ASC")
    fun getAll(wsId: String): Flow<List<CategoriaEntity>>

    @Upsert
    suspend fun upsertAll(categorias: List<CategoriaEntity>)

    @Query("DELETE FROM categorias WHERE workspaceId = :wsId")
    suspend fun deleteAll(wsId: String)
}

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimientos WHERE workspaceId = :wsId ORDER BY creadoEn DESC")
    fun getAll(wsId: String): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE workspaceId = :wsId AND tipo = :tipo ORDER BY creadoEn DESC")
    fun getByTipo(wsId: String, tipo: String): Flow<List<MovimientoEntity>>

    @Insert
    suspend fun insert(movimiento: MovimientoEntity)

    @Query("DELETE FROM movimientos WHERE workspaceId = :wsId")
    suspend fun deleteAll(wsId: String)
}

// ═══════════════════════════════════════════════════════════════
// ROOM DATABASE
// ═══════════════════════════════════════════════════════════════

@Database(
    entities = [
        CategoriaEntity::class,
        ProductoEntity::class,
        MovimientoEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class InventarioDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun movimientoDao(): MovimientoDao
}
```

---

## 4. Mapping entre Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                    MAPPING FLOW                                 │
│                                                                 │
│  API Response (JSON)                                            │
│       │                                                         │
│       ▼                                                         │
│  DTO (@Serializable)                                            │
│       │  .toDomain()                                            │
│       ▼                                                         │
│  Domain Model (pure Kotlin)                                     │
│       │  .toEntity()                                            │
│       ▼                                                         │
│  Room Entity                                                    │
│       │  .toDomain()                                            │
│       ▼                                                         │
│  Domain Model (para UI)                                         │
│                                                                 │
│  Ejemplo:                                                       │
│  ProductoDto.toDomain() → Producto                              │
│  Producto.toEntity()    → ProductoEntity                        │
│  ProductoEntity.toDomain() → Producto (con cache data)          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```
