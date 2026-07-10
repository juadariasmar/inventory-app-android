# Casos de Uso — InventarioApp Android

---

## 1. Diagrama de Actores

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        DIAGRAMA DE ACTORES                             │
│                        (Notación UML)                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│                          ┌──────────────┐                              │
│                          │   Sistema    │                              │
│                          │  Inventario  │                              │
│                          │    (API)     │                              │
│                          └──────┬───────┘                              │
│                                 │                                       │
│          ┌──────────────────────┼──────────────────────┐               │
│          │                      │                      │               │
│          ▼                      ▼                      ▼               │
│   ┌─────────────┐      ┌─────────────┐      ┌─────────────┐          │
│   │   Usuario   │      │    Admin    │      │  Neon Auth  │          │
│   │   Móvil     │      │   (Web)     │      │  (Sistema)  │          │
│   │             │      │             │      │             │          │
│   │ - Login     │      │ - Invita    │      │ - Autentica │          │
│   │ - Consulta  │      │ - Configura │      │ - Sesiones  │          │
│   │ - Opera     │      │ - Gestiona  │      │ - Tokens    │          │
│   └─────────────┘      └─────────────┘      └─────────────┘          │
│                                                                         │
│   ACTOR PRIMARIO:     Usuario Móvil                                    │
│   ACTOR SECUNDARIO:   Admin (realiza acciones en web que afectan móvil)│
│   ACTOR SISTEMA:      Neon Auth (provee autenticación)                │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Tabla de Casos de Uso

### 2.1 Autenticación y Acceso

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC01 | Login | Cualquier usuario | **Crítica** | Autenticar con email y password contra Neon Auth. Obtener Bearer token para operaciones subsecuentes. | Usuario tiene cuenta en Neon Auth |
| UC02 | Seleccionar Organización | Cualquier usuario | **Crítica** | Mostrar lista de organizaciones donde el usuario tiene `OrgMembership`. Permitir seleccionar una. | UC01 completado |
| UC03 | Seleccionar Workspace | Cualquier usuario | **Crítica** | Mostrar workspaces accesibles dentro de la organización seleccionada. OWNER/ADMIN ven todos, MEMBER solo los con `WSMembership`. | UC02 completado |
| UC04 | Cerrar Sesión | Cualquier usuario | **Alta** | Limpiar token de almacenamiento seguro, invalidar sesión en servidor, navegar a login. | Sesión activa |

### 2.2 Inventario — Productos

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC05 | Listar Productos | Cualquier usuario activo | **Alta** | Obtener lista paginada de productos del workspace. Búsqueda por nombre/código. Filtro por categoría. Indicador de stock bajo. | UC03 completado |
| UC06 | Ver Detalle de Producto | Cualquier usuario activo | **Alta** | Mostrar información completa de un producto: código, nombre, descripción, categoría, precio, stock, stock mínimo, historial de precios. | UC05 completado |
| UC07 | Crear Producto | Cualquier usuario activo | **Alta** | Formulario con validación: código (requerido, único en workspace), nombre (requerido), categoría (requerido), precio (>0), stock mínimo (≥0). | UC03 completado |
| UC08 | Editar Producto | Cualquier usuario activo | **Alta** | Mismo formulario que crear, prellenado con datos existentes. Cambios de precio generan `HistorialPrecio`. | UC05 completado |
| UC09 | Eliminar Producto | Solo ADMIN | **Media** | Confirmación antes de eliminar. No permitir si tiene movimientos o ventas asociadas (validación backend). | UC05 completado, rol ADMIN |

### 2.3 Inventario — Categorías

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC10 | Listar Categorías | Cualquier usuario activo | **Alta** | Obtener lista de categorías del workspace con conteo de productos asociados. | UC03 completado |
| UC11 | Crear/Editar Categoría | Cualquier usuario activo | **Alta** | Formulario: nombre (requerido, único en workspace), prefijo (requerido, único en workspace, máx 5 caracteres). | UC03 completado |

### 2.4 Inventario — Movimientos

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC12 | Registrar Movimiento | Con permiso `REGISTRAR_MOVIMIENTOS` | **Crítica** | Formulario: tipo (entrada/salida), producto (selector con búsqueda), cantidad (>0, ≤stock para salidas), notas (opcional). Validación de stock en tiempo real. | UC03 completado, permiso asignado |
| UC13 | Ver Movimientos | Cualquier usuario activo | **Alta** | Lista paginada de movimientos. Filtro por tipo (entrada/salida/todos). Muestra: producto, tipo, cantidad, usuario, fecha. | UC03 completado |

### 2.5 Ventas

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC14 | Crear Venta | Con permiso `REALIZAR_VENTAS` | **Alta** | Formulario multi-item: cliente (opcional, selector con búsqueda), items (producto + cantidad + precio), notas (opcional). Total calculado automáticamente. Stock descontado transaccionalmente. | UC03 completado, permiso asignado |
| UC15 | Ver Detalle de Venta | Cualquier usuario activo | **Media** | Mostrar: fecha, vendedor, cliente, items con subtotales, total, estado. | UC03 completado |
| UC16 | Cancelar Venta | Solo ADMIN | **Media** | Confirmación con motivo de cancelación. Stock restaurado. Estado cambiado a CANCELADA. | UC15 completado, rol ADMIN |
| UC17 | Listar Ventas | Cualquier usuario activo | **Media** | Lista paginada de ventas. Muestra: fecha, vendedor, cliente, total, estado. | UC03 completado |

### 2.6 Clientes

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC18 | Listar Clientes | Cualquier usuario activo | **Media** | Lista con búsqueda por nombre. Muestra: nombre, documento, email, teléfono. | UC03 completado |
| UC19 | Crear/Editar Cliente | Cualquier usuario activo | **Media** | Formulario: nombre (requerido), documento (único), email, teléfono, dirección, notas. | UC03 completado |

### 2.7 Dashboard

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC20 | Ver Dashboard | Cualquier usuario activo | **Alta** | Métricas del workspace: total productos, total unidades, productos con stock bajo, valor total del inventario. Últimos 10 movimientos. | UC03 completado |

### 2.8 Notificaciones y Configuración

| ID | Caso de Uso | Actor | Prioridad | Descripción | Precondiciones |
|----|-------------|-------|-----------|-------------|----------------|
| UC21 | Ver Notificaciones | Cualquier usuario activo | **Media** | Lista de notificaciones del workspace. Indicador de no leídas. | UC03 completado |
| UC22 | Ver/Editar Perfil | Cualquier usuario activo | **Baja** | Mostrar: nombre, email, rol, permisos. Editar nombre. | UC03 completado |
| UC23 | Cambiar Tema | Cualquier usuario activo | **Baja** | Toggle modo claro/oscuro. Persistido en DataStore local. | Ninguna |

---

## 3. Diagrama de Casos de Uso General

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    SISTEMA: InventarioApp Android                      │
│                    (Extensión Móvil del Sistema Web)                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│    ┌─────────────────────────────────────────────────────────────┐     │
│    │                    MODULO: AUTH                             │     │
│    │  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐                  │     │
│    │  │ UC01 │  │ UC02 │  │ UC03 │  │ UC04 │                  │     │
│    │  │Login │  │Sel.  │  │Sel.  │  │Logout│                  │     │
│    │  │      │  │Org   │  │WS    │  │      │                  │     │
│    │  └──┬───┘  └──┬───┘  └──┬───┘  └──┬───┘                  │     │
│    └─────┼─────────┼─────────┼─────────┼───────────────────────┘     │
│          │         │         │         │                               │
│  ┌───────┴─────────┴─────────┴─────────┴───────────────────────┐     │
│  │                      ACTOR: Usuario Móvil                   │     │
│  └───────┬─────────┬─────────┬─────────┬───────────────────────┘     │
│          │         │         │         │                               │
│    ┌─────┼─────────┼─────────┼─────────┼───────────────────────┐     │
│    │     │         │         │         │                       │     │
│    │  ┌──┴───┐  ┌──┴───┐  ┌─┴────┐  ┌─┴────┐                │     │
│    │  │ UC05 │  │ UC10 │  │ UC12 │  │ UC14 │                │     │
│    │  │List. │  │List. │  │Reg.  │  │Crear │                │     │
│    │  │Prod. │  │Categ.│  │Movim.│  │Venta │                │     │
│    │  └──┬───┘  └──┬───┘  └──┬───┘  └──┬───┘                │     │
│    │     │         │         │         │                       │     │
│    │  ┌──┴───┐  ┌──┴───┐  ┌─┴────┐  ┌─┴────┐  ┌──────────┐ │     │
│    │  │ UC06 │  │ UC11 │  │ UC13 │  │ UC15 │  │ UC18     │ │     │
│    │  │Det.  │  │Crear │  │List. │  │Det.  │  │List.     │ │     │
│    │  │Prod. │  │Categ.│  │Movim.│  │Venta │  │Clientes  │ │     │
│    │  └──┬───┘  └──────┘  └──────┘  └──┬───┘  └──┬───────┘ │     │
│    │     │                              │         │           │     │
│    │  ┌──┴───┐                       ┌──┴───┐  ┌──┴───┐     │     │
│    │  │ UC07 │                       │ UC16 │  │ UC19 │     │     │
│    │  │Crear │                       │Canc. │  │Crear │     │     │
│    │  │Prod. │                       │Venta │  │Client│     │     │
│    │  └──┬───┘                       └──────┘  └──────┘     │     │
│    │     │                                                    │     │
│    │  ┌──┴───┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐    │     │
│    │  │ UC08 │  │ UC09 │  │ UC20 │  │ UC21 │  │ UC22 │    │     │
│    │  │Edit. │  │Elim. │  │Dash- │  │Notif.│  │Perfil│    │     │
│    │  │Prod. │  │Prod. │  │board │  │      │  │      │    │     │
│    │  └──────┘  └──────┘  └──────┘  └──────┘  └──┬───┘    │     │
│    │                                               │         │     │
│    │                                          ┌────┴───┐    │     │
│    │                                          │ UC23   │    │     │
│    │                                          │ Tema   │    │     │
│    │                                          └────────┘    │     │
│    └────────────────────────────────────────────────────────┘     │
│                                                                     │
│    ACTOR SECUNDARIO: Admin (web)                                    │
│    ┌─────────────────────────────────────────────────────────┐     │
│    │ - Invita usuarios (precondición para UC01-UC03)        │     │
│    │ - Gestiona workspaces (precondición para UC02-UC03)    │     │
│    │ - Asigna permisos (precondición para UC12, UC14)       │     │
│    └─────────────────────────────────────────────────────────┘     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 4. Diagramas de Flujo (Casos Clave)

### UC01: Login

```
┌─────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Usuario  │     │ LoginScreen  │     │ LoginVM      │     │ NeonAuth API │
└────┬────┘     └──────┬───────┘     └──────┬───────┘     └──────┬───────┘
     │                  │                    │                    │
     │ 1.Ingresar       │                    │                    │
     │   email+password │                    │                    │
     │─────────────────>│                    │                    │
     │                  │ 2.onLogin()        │                    │
     │                  │───────────────────>│                    │
     │                  │                    │ 3.POST /sign-in    │
     │                  │                    │───────────────────>│
     │                  │                    │                    │
     │                  │                    │ 4.{token, user}    │
     │                  │                    │<───────────────────│
     │                  │                    │                    │
     │                  │                    │ 5.saveToken()      │
     │                  │                    │──────┐             │
     │                  │                    │      │ DataStore   │
     │                  │                    │<─────┘             │
     │                  │                    │                    │
     │                  │ 6.UiState.Success  │                    │
     │                  │<───────────────────│                    │
     │                  │                    │                    │
     │ 7.Navegar a      │                    │                    │
     │   Org Selector   │                    │                    │
     │<─────────────────│                    │                    │
     │                  │                    │                    │
```

### UC12: Registrar Movimiento

```
┌─────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Usuario  │     │ MovFormScr   │     │ MovVM        │     │ Next.js API  │
└────┬────┘     └──────┬───────┘     └──────┬───────┘     └──────┬───────┘
     │                  │                    │                    │
     │ 1.Seleccionar    │                    │                    │
     │   tipo/producto/ │                    │                    │
     │   cantidad       │                    │                    │
     │─────────────────>│                    │                    │
     │                  │ 2.onRegistrar()    │                    │
     │                  │───────────────────>│                    │
     │                  │                    │ 3.POST /movimientos│
     │                  │                    │  Bearer: <token>   │
     │                  │                    │───────────────────>│
     │                  │                    │                    │
     │                  │                    │ 4.{movimiento}     │
     │                  │                    │<───────────────────│
     │                  │                    │                    │
     │                  │                    │ 5.updateRoomCache()│
     │                  │                    │──────┐             │
     │                  │                    │      │ Room DB     │
     │                  │                    │<─────┘             │
     │                  │                    │                    │
     │                  │ 6.Result.Success   │                    │
     │                  │<───────────────────│                    │
     │                  │                    │                    │
     │ 7.Snackbar       │                    │                    │
     │   "Movimiento    │                    │                    │
     │    registrado"   │                    │                    │
     │<─────────────────│                    │                    │
     │                  │                    │                    │
     │ 8.Navegar a      │                    │                    │
     │   lista          │                    │                    │
     │<─────────────────│                    │                    │
```

---

## 5. Priorización y Fases

```
FASE 1: CORE (4 días)
├── UC01: Login
├── UC02: Seleccionar Organización
├── UC03: Seleccionar Workspace
├── UC04: Cerrar Sesión
└── UC20: Dashboard

FASE 2: INVENTARIO (4 días)
├── UC05: Listar Productos
├── UC06: Ver Detalle Producto
├── UC07: Crear Producto
├── UC08: Editar Producto
├── UC09: Eliminar Producto
├── UC10: Listar Categorías
├── UC11: Crear/Editar Categoría
├── UC12: Registrar Movimiento
└── UC13: Ver Movimientos

FASE 3: VENTAS + CLIENTES (3 días)
├── UC14: Crear Venta
├── UC15: Ver Detalle Venta
├── UC16: Cancelar Venta
├── UC17: Listar Ventas
├── UC18: Listar Clientes
└── UC19: Crear/Editar Cliente

FASE 4: POLISH (2 días)
├── UC21: Ver Notificaciones
├── UC22: Ver/Editar Perfil
└── UC23: Cambiar Tema
```
