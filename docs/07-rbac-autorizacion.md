# RBAC y Autorizacion — InventarioApp Android

---

## 1. Diagrama Jerarquico de Permisos

```
┌─────────────────────────────────────────────────────────────────────┐
│              JERARQUIA DE PERMISOS (3 niveles)                      │
│              (Misma implementacion que el Web)                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  NIVEL 1: GLOBAL (Usuario.rol)                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                             │   │
│  │  SUPER_ADMIN                                                │   │
│  │    ├── Bypass TOTAL de permisos                             │   │
│  │    ├── Accede a /api/admin/*                                │   │
│  │    ├── Forzado por ADMIN_EMAILS (no se puede degradar)     │   │
│  │    └── Puede impersonar otros usuarios                      │   │
│  │                                                             │   │
│  │  ADMIN                                                      │   │
│  │    ├── Bypass TOTAL de permisos                             │   │
│  │    ├── CRUD completo de productos, categorias, proveedores  │   │
│  │    ├── Gestion de usuarios dentro de su org                 │   │
│  │    ├── Crear invitaciones                                   │   │
│  │    └── Cancelar ventas                                      │   │
│  │                                                             │   │
│  │  USUARIO                                                    │   │
│  │    ├── Sin bypass                                           │   │
│  │    ├── Permisos granulares via Usuario.permisos[]           │   │
│  │    └── Solo puede realizar operaciones permitidas           │   │
│  │                                                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  NIVEL 2: ORGANIZACION (OrgMembership.role)                        │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                             │   │
│  │  OWNER                                                      │   │
│  │    └── Acceso automatico a TODOS los workspaces de la org   │   │
│  │                                                             │   │
│  │  ADMIN                                                      │   │
│  │    └── Acceso automatico a TODOS los workspaces de la org   │   │
│  │                                                             │   │
│  │  MEMBER                                                     │   │
│  │    └── Solo workspaces con WSMembership explícita           │   │
│  │                                                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  NIVEL 3: WORKSPACE (WSMembership.role)                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                             │   │
│  │  WS-Admin                                                   │   │
│  │    ├── read:inventory, write:inventory, delete:inventory    │   │
│  │    └── manage:ws-settings                                   │   │
│  │                                                             │   │
│  │  WS-Editor                                                  │   │
│  │    └── read:inventory, write:inventory                      │   │
│  │                                                             │   │
│  │  WS-Viewer                                                  │   │
│  │    └── read:inventory                                       │   │
│  │                                                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  REGLA CLAVE:                                                      │
│  Si eres ADMIN o SUPER_ADMIN → tienePermiso() retorna TRUE         │
│  SIEMPRE, sin importar el array de permisos.                       │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. Flujo de Autorizacion en el Android

```
┌─────────────────────────────────────────────────────────────────────┐
│                    FLUJO COMPLETO                                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────┐                                                      │
│  │  Login   │                                                      │
│  └────┬─────┘                                                      │
│       │                                                            │
│       ▼                                                            │
│  Neon Auth valida email/password                                   │
│       │                                                            │
│       ▼                                                            │
│  POST /api/auth/sign-in/email                                      │
│       │                                                            │
│       ├── Response header: set-auth-token                          │
│       └── Response body: { token, user: { id, email, name } }     │
│       │                                                            │
│       ▼                                                            │
│  Backend resuelve sesion internamente:                             │
│       │                                                            │
│       ├── 1. Busca Usuario por neonAuthId                          │
│       ├── 2. Fallback: busca por email                             │
│       ├── 3. Si no existe → crea tenant auto (Path B)              │
│       └── 4. Si existe → retorna { rol, estado, permisos }        │
│       │                                                            │
│       ▼                                                            │
│  Android guarda token en EncryptedSharedPreferences                │
│       │                                                            │
│       ▼                                                            │
│  GET /api/auth/get-session                                         │
│       │                                                            │
│       └── { user: { id, email, name } }                            │
│       │                                                            │
│       ▼                                                            │
│  Android obtiene organizaciones:                                   │
│       │                                                            │
│       GET /api/organizations                                       │
│       │                                                            │
│       └── lista de orgs donde el usuario tiene OrgMembership      │
│       │                                                            │
│       ▼                                                            │
│  Seleccionar Org → obtener workspaces:                             │
│       │                                                            │
│       GET /api/{orgSlug}/workspaces                                │
│       │                                                            │
│       └── workspaces accesibles                                    │
│           (OWNER/ADMIN = todos, MEMBER = solo los propios)        │
│       │                                                            │
│       ▼                                                            │
│  Seleccionar WS → guardar en DataStore                             │
│       │                                                            │
│       ▼                                                            │
│  Todas las operaciones:                                            │
│       │                                                            │
│       /api/{orgSlug}/{wsSlug}/...                                  │
│       Header: Authorization: Bearer <token>                        │
│       │                                                            │
│       ▼                                                            │
│  Backend verifica en CADA route:                                   │
│       │                                                            │
│       ├── obtenerSesion() → resuelve usuario                       │
│       ├── resolverContextoTenant() → valida org+ws access          │
│       └── esAdmin() o tienePermiso() → para operacion             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Permisos por Operacion

| Operacion | Endpoint HTTP | Permiso Requerido | Actor |
|-----------|--------------|-------------------|-------|
| Ver productos | `GET /productos` | Cualquier usuario activo en WS | Todos |
| Crear producto | `POST /productos` | esAdmin() o cualquier | Todos |
| Editar producto | `PUT /productos/{id}` | esAdmin() o cualquier | Todos |
| Eliminar producto | `DELETE /productos/{id}` | esAdmin() | Solo Admin |
| Ver categorias | `GET /categorias` | Cualquier usuario activo en WS | Todos |
| Crear categoria | `POST /categorias` | esAdmin() o cualquier | Todos |
| Editar categoria | `PUT /categorias/{id}` | esAdmin() o cualquier | Todos |
| Eliminar categoria | `DELETE /categorias/{id}` | esAdmin() | Solo Admin |
| Registrar movimiento | `POST /movimientos` | tienePermiso(REGISTRAR_MOVIMIENTOS) | Con permiso |
| Ver movimientos | `GET /movimientos` | Cualquier usuario activo en WS | Todos |
| Crear venta | `POST /ventas` | tienePermiso(REALIZAR_VENTAS) | Con permiso |
| Ver venta | `GET /ventas/{id}` | Cualquier usuario activo en WS | Todos |
| Cancelar venta | `POST /ventas/{id}/cancelar` | esAdmin() | Solo Admin |
| Listar ventas | `GET /ventas` | Cualquier usuario activo en WS | Todos |
| Crear cliente | `POST /clientes` | Cualquier usuario activo en WS | Todos |
| Editar cliente | `PUT /clientes/{id}` | Cualquier usuario activo en WS | Todos |
| Ver clientes | `GET /clientes` | Cualquier usuario activo en WS | Todos |
| Dashboard | `GET /analisis` | tienePermiso(VER_ANALISIS) | Con permiso |
| Notificaciones | `GET /notificaciones` | Cualquier usuario activo en WS | Todos |

---

## 4. Permisos Granulares (Usuario.permisos[])

```kotlin
// Mismos valores que el Prisma enum Permiso
enum class Permiso {
    VER_ANALISIS,
    EXPORTAR_REPORTES,
    REGISTRAR_MOVIMIENTOS,
    REALIZAR_VENTAS,
    AGREGAR_STOCK,
    DESCONTAR_STOCK
}

// Mapping a UI
fun Sesion.puedeRegistrarMovimientos(): Boolean {
    return rol in listOf("SUPER_ADMIN", "ADMIN") ||
           permisos.contains("REGISTRAR_MOVIMIENTOS")
}

fun Sesion.puedeRealizarVentas(): Boolean {
    return rol in listOf("SUPER_ADMIN", "ADMIN") ||
           permisos.contains("REALIZAR_VENTAS")
}

fun Sesion.puedeVerAnalisis(): Boolean {
    return rol in listOf("SUPER_ADMIN", "ADMIN") ||
           permisos.contains("VER_ANALISIS")
}

fun Sesion.esAdmin(): Boolean {
    return rol in listOf("SUPER_ADMIN", "ADMIN")
}
```

---

## 5. UI Permission Handling

```
┌─────────────────────────────────────────────────────────────────────┐
│                 UI PERMISSION HANDLING                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. After login, session contains: rol + permisos[]                │
│                                                                     │
│  2. Android stores session in ViewModel scope                      │
│                                                                     │
│  3. UI elements conditionally rendered:                            │
│     ┌─────────────────────────────────────────────────────────┐    │
│     │ Elemento              │ Condicion                        │    │
│     ├───────────────────────┼──────────────────────────────────┤    │
│     │ FAB "Nuevo"           │ canWrite (admin o permiso)       │    │
│     │ Boton "Eliminar"      │ esAdmin()                        │    │
│     │ Form Movimiento       │ puedeRegistrarMovimientos()      │    │
│     │ Form Venta            │ puedeRealizarVentas()            │    │
│     │ Dashboard metrics     │ puedeVerAnalisis()               │    │
│     │ Cancelar venta        │ esAdmin()                        │    │
│     └─────────────────────────────────────────────────────────┘    │
│                                                                     │
│  4. Backend always validates (defense-in-depth):                   │
│     - Even if UI shows a button, backend can reject                │
│     - 403 response → show error snackbar                           │
│                                                                     │
│  5. Role changes take effect on next session:                      │
│     - If admin revokes permissions, next API call fails            │
│     - Android should re-fetch session periodically                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**Implementacion en Compose:**

```kotlin
@Composable
fun ProductosScreen(
    viewModel: ProductosViewModel = hiltViewModel(),
    sesion: Sesion = LocalSesion.current
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            // Only show FAB if user can write
            if (sesion.esAdmin() || sesion.puedeRegistrarMovimientos()) {
                FloatingActionButton(onClick = { /* navigate to form */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Nuevo producto")
                }
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(uiState.productos) { producto ->
                ProductCard(
                    producto = producto,
                    onDelete = if (sesion.esAdmin()) {
                        { viewModel.onDelete(producto.id) }
                    } else null
                )
            }
        }
    }
}
```

---

## 6. Estados de Usuario (Practica Real)

```
┌─────────────────────────────────────────────────────────────────────┐
│  ESTADO REAL: Solo ACTIVO se usa en la practica                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  PENDIENTE                                                          │
│    └── Efectivamente MUERTO. Todos los caminos de creacion         │
│        (webhook, invitacion, onboarding) setean                     │
│        estado = ACTIVO directamente.                                │
│        Solo existe en el schema de Prisma por legacy.               │
│                                                                     │
│  ACTIVO                                                             │
│    └── Estado normal. El usuario puede operar.                      │
│        Es el unico estado asignado en la practica.                  │
│                                                                     │
│  SUSPENDIDO                                                         │
│    └── Solo por accion manual de admin.                             │
│        Bloquea todo acceso.                                         │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│  IMPLICACION PARA ANDROID:                                          │
│                                                                     │
│  - NO implementar pantalla de "esperando aprobacion"               │
│  - Si usuario existe y es ACTIVO → permitir acceso                 │
│  - Si usuario no existe → crear tenant (Path B del webhook)        │
│  - Si SUSPENDIDO → mostrar mensaje y redirigir a login             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 7. Flujo de Invitacion (Referencia Web)

```
┌─────────────────────────────────────────────────────────────────────┐
│  FLUJO DE INVITACION (web) que genera acceso en movil              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. Admin (web) envia invitacion                                   │
│     POST /api/{org}/{ws}/invitaciones                              │
│     → Se envia email con token                                     │
│                                                                     │
│  2. Usuario hace click en link                                     │
│     GET /api/invitaciones/validar?token=...                        │
│     → Retorna { valida: true, email, empresaNombre }               │
│                                                                     │
│  3. Usuario se loguea via Neon Auth                                │
│     → Webhook POST /api/webhooks/neon (user.created)               │
│     → Webhook crea Usuario: { estado: ACTIVO, rol: USUARIO }      │
│                                                                     │
│  4. Frontend acepta invitacion                                     │
│     POST /api/invitaciones/aceptar                                 │
│     → Crea OrgMembership                                           │
│     → onboardingCompletado = true                                  │
│                                                                     │
│  5. Usuario queda ACTIVO con permisos asignados                    │
│     → Puede usar la app movil inmediatamente                       │
│                                                                     │
│  CAMINO DEL MOVIL:                                                  │
│  Login → Busca Usuario por neonAuthId → Lo encuentra (ACTIVO)      │
│  → Obtiene OrgMemberships → Selecciona org → Selecciona ws         │
│  → Opera normalmente                                               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```
