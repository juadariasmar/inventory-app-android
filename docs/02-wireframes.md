# Wireframes — InventarioApp Android

---

## 1. Sistema de Diseño

### 1.1 Paleta de Colores

| Token | Valor | Uso |
|-------|-------|-----|
| `primary` | `#1B5E20` | Verde oscuro — acciones principales, AppBar |
| `onPrimary` | `#FFFFFF` | Texto sobre primario |
| `primaryContainer` | `#A5D6A7` | Fondos de badges, chips |
| `secondary` | `#F57F17` | Amber — alertas, stock bajo |
| `error` | `#B71C1C` | Rojo — salidas, eliminar, errores |
| `background` (light) | `#FAFAFA` | Fondo general |
| `background` (dark) | `#121212` | Fondo general oscuro |
| `surface` (light) | `#FFFFFF` | Cards, sheets |
| `surface` (dark) | `#1E1E1E` | Cards, sheets |
| `onSurface` | `#1C1B1F` | Texto principal |
| `outline` | `#79747E` | Bordes de campos |

### 1.2 Tipografía

| Estilo | Tamaño | Peso | Uso |
|--------|--------|------|-----|
| Headline Large | 24sp | Bold | Títulos de pantalla |
| Title Medium | 18sp | Medium | Secciones, cards |
| Body Large | 16sp | Regular | Texto principal |
| Body Medium | 14sp | Regular | Texto secundario |
| Label Large | 14sp | Medium | Botones |
| Label Small | 12sp | Regular | Badges, captions |

### 1.3 Espaciado

| Token | Valor | Uso |
|-------|-------|-----|
| `spacing_xs` | 4dp | Entre elementos compactos |
| `spacing_sm` | 8dp | Entre elementos relacionados |
| `spacing_md` | 16dp | Padding general de pantalla |
| `spacing_lg` | 24dp | Entre secciones |
| `spacing_xl` | 32dp | Separación major |

---

## 2. Wireframes por Pantalla

### 2.1 Login Screen (UC01)

```
┌──────────────────────────────────────┐
│          (status bar)               │
├──────────────────────────────────────┤
│                                      │
│                                      │
│                                      │
│              📦                      │
│        InventarioApp                 │
│    Sistema de Inventarios            │
│                                      │
│                                      │
│  ┌────────────────────────────────┐  │
│  │  Email                         │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │  Contraseña               👁   │  │
│  └────────────────────────────────┘  │
│                                      │
│                                      │
│  ┌────────────────────────────────┐  │
│  │         Iniciar Sesión         │  │
│  └────────────────────────────────┘  │
│                                      │
│         ¿Olvidaste contraseña?       │
│                                      │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `Scaffold` con `contentPadding`
- `Column` centrado verticalmente
- `Icon` (InventarioApp logo, 64dp)
- `Text` (Headline Large, "InventarioApp")
- `Text` (Body Medium, "Sistema de Inventarios", `onSurfaceVariant`)
- `OutlinedTextField` (email, `KeyboardType.Email`, leading icon `Email`)
- `OutlinedTextField` (password, `VisualTransformation.Password`, trailing icon toggle)
- `Button` (filled, full-width, `enabled = !uiState.isLoading`)
- `TextButton` (text link, `Color.primary`)
- `CircularProgressIndicator` (overlay when loading)
- `SnackbarHost` (error messages)

---

### 2.2 Organization Selector (UC02)

```
┌──────────────────────────────────────┐
│          (status bar)               │
├──────────────────────────────────────┤
│                                      │
│              📦                      │
│        InventarioApp                 │
│                                      │
│  Selecciona tu empresa:              │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 🏢  Mi Empresa S.A.           │  │
│  │     3 workspaces · Plan FREE  │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 🏢  Otra Empresa Ltda         │  │
│  │     1 workspace · Plan PRO    │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 🏢  Startup Tech             │  │
│  │     5 workspaces · Plan FREE  │  │
│  └────────────────────────────────┘  │
│                                      │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `Column` centrado
- `LazyColumn` (lista de orgs)
- `Card` (elevada, `onClick → navigate to WS selector`)
  - `Row` con `Icon` (Business) + `Column` (nombre, conteo ws + plan badge)
- `CircularProgressIndicator` (loading state)

---

### 2.3 Workspace Selector (UC03)

```
┌──────────────────────────────────────┐
│  ←  Mi Empresa S.A.                 │
├──────────────────────────────────────┤
│                                      │
│  Selecciona workspace:               │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 📋  Bodega Principal          │  │
│  │     142 productos              │  │
│  │     Tu rol: Admin              │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 📋  Sucursal Norte            │  │
│  │     89 productos               │  │
│  │     Tu rol: Editor             │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ 📋  Almacén Central           │  │
│  │     256 productos              │  │
│  │     Tu rol: Viewer             │  │
│  └────────────────────────────────┘  │
│                                      │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (navigationIcon: ArrowBack, title: org name)
- `LazyColumn` (lista de workspaces)
- `Card` (elevada)
  - `Row` con `Icon` (FolderOpen) + `Column` (nombre, conteo productos, rol badge)
  - `Badge` con color según rol (Admin=primary, Editor=secondary, Viewer=outline)
- `CircularProgressIndicator` (loading state)

---

### 2.4 Home / Dashboard (UC20)

```
┌──────────────────────────────────────┐
│  ☰   Bodega Principal          🔔   │
├──────────────────────────────────────┤
│                                      │
│  ┌──────────────┬──────────────┐     │
│  │  Productos   │  Unidades    │     │
│  │     142      │    3,850     │     │
│  └──────────────┴──────────────┘     │
│  ┌──────────────┬──────────────┐     │
│  │ Stock Bajo   │  Valor       │     │
│  │      8       │  $12.5M      │     │
│  └──────────────┴──────────────┘     │
│                                      │
│  Últimos Movimientos                 │
│  ┌────────────────────────────────┐  │
│  │ ↑ Entrada    x50              │  │
│  │ Widget ABC                     │  │
│  │ Juan Pérez · 10:30 a.m.       │  │
│  ├────────────────────────────────┤  │
│  │ ↓ Salida    x10               │  │
│  │ Cable HDMI 2m                 │  │
│  │ María López · 9:15 a.m.       │  │
│  ├────────────────────────────────┤  │
│  │ ↑ Entrada    x200             │  │
│  │ Mouse Inalámbrico             │  │
│  │ Carlos Ruiz · 8:00 a.m.       │  │
│  └────────────────────────────────┘  │
│                                      │
├──────────────────────────────────────┤
│  🏠      📦      ➕      📋     ⚙️   │
│ Home  Productos Nuevo Movim. Config  │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (navigationIcon: Menu, title: workspace name, action: NotificationBell)
- `LazyVerticalGrid` (columns: Fixed(2), contentPadding: 16dp)
  - `MetricCard` × 4 (icon, value, label, color-coded)
- `LazyColumn` (recent movements)
  - `MovementItem` (type icon with color, product name, quantity, user, time)
- `NavigationBar` (5 items)
  - `NavigationBarItem` × 5 (Home, Productos, FAB trigger, Movimientos, Settings)

---

### 2.5 Product List (UC05)

```
┌──────────────────────────────────────┐
│  ←  Productos                  🔍 ⋮ │
├──────────────────────────────────────┤
│  ┌────────────────────────────────┐  │
│  │ 🔍  Buscar producto...        │  │
│  └────────────────────────────────┘  │
│                                      │
│  Filtrar: [Todos ▼]                  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ Widget ABC                     │  │
│  │ CAT-001 · Electrónicos        │  │
│  │ Stock: 45      $25,000        │  │
│  ├────────────────────────────────┤  │
│  │ Cable HDMI 2m      ⚠️         │  │
│  │ CAT-002 · Electrónicos        │  │
│  │ Stock: 3       $15,000        │  │
│  ├────────────────────────────────┤  │
│  │ Mouse Inalámbrico             │  │
│  │ CAT-001 · Electrónicos        │  │
│  │ Stock: 120     $35,000        │  │
│  ├────────────────────────────────┤  │
│  │ Teclado Mecánico              │  │
│  │ CAT-003 · Periféricos         │  │
│  │ Stock: 67      $85,000        │  │
│  └────────────────────────────────┘  │
│                                      │
│                       ┌──────────┐   │
│                       │ + Nuevo  │   │
│                       │ Producto │   │
│                       └──────────┘   │
├──────────────────────────────────────┤
│  🏠      📦      ➕      📋     ⚙️   │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (back, title, search toggle, overflow menu)
- `SearchBar` (expandable, `OutlinedTextField` con lupa)
- `FilterChip` (category filter dropdown)
- `LazyColumn` (product list, `item` with `Modifier.clickable`)
- `ProductCard`
  - `Row` con `Column` (nombre, código + categoría) + `Column` (stock badge, precio)
  - `StockBadge`: `Surface` con color (green > min, yellow = min, red < min)
  - `Text` precio formateado COP
- `FloatingActionButton` (add, `ExtendedFloatingActionButton` con texto)

---

### 2.6 Product Form — Create/Edit (UC07/UC08)

```
┌──────────────────────────────────────┐
│  ←  Guardar                          │
├──────────────────────────────────────┤
│                                      │
│  Código*:                            │
│  ┌────────────────────────────────┐  │
│  │  PROD-001                      │  │
│  └────────────────────────────────┘  │
│  Ej: Elect-001                      │
│                                      │
│  Nombre*:                            │
│  ┌────────────────────────────────┐  │
│  │  Widget ABC                    │  │
│  └────────────────────────────────┘  │
│                                      │
│  Descripción:                        │
│  ┌────────────────────────────────┐  │
│  │  Widget de alta calidad para   │  │
│  │  aplicaciones industriales     │  │
│  └────────────────────────────────┘  │
│                                      │
│  Categoría*:                         │
│  ┌────────────────────────────────┐  │
│  │  Electrónicos              ▼   │  │
│  └────────────────────────────────┘  │
│                                      │
│  Precio*:              Stock Mínimo: │
│  ┌────────────────┐ ┌────────────┐  │
│  │  $ 25,000      │ │     10     │  │
│  └────────────────┘ └────────────┘  │
│                                      │
│  Stock Actual:                       │
│  ┌────────────────────────────────┐  │
│  │            45                  │  │
│  └────────────────────────────────┘  │
│                                      │
│                                      │
│  ┌────────────────────────────────┐  │
│  │           Guardar              │  │
│  └────────────────────────────────┘  │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (back, action: save text button)
- `LazyColumn` (scrollable form)
- `OutlinedTextField` × 5 (código, nombre, descripción, precio, stock mínimo)
  - Cada campo: `label`, `supportingText` (error si invalid), `isError`
- `ExposedDropdownMenuBox` (categoría selector)
- `Button` (save, full-width, `enabled = form.isValid && !isLoading`)
- Validación: código requerido, nombre requerido, categoría requerida, precio > 0, stock mínimo ≥ 0

---

### 2.7 Movement Form (UC12)

```
┌──────────────────────────────────────┐
│  ←  Registrar Movimiento             │
├──────────────────────────────────────┤
│                                      │
│  Tipo:                               │
│  ┌──────────────┬──────────────┐     │
│  │   ENTRADA    │    SALIDA    │     │
│  └──────────────┴──────────────┘     │
│                                      │
│  Producto*:                          │
│  ┌────────────────────────────────┐  │
│  │ 🔍  Buscar producto...        │  │
│  ├────────────────────────────────┤  │
│  │ Widget ABC          Stock: 45  │  │
│  │ Cable HDMI 2m       Stock: 3  │  │
│  │ Mouse Inalámbrico   Stock: 120│  │
│  └────────────────────────────────┘  │
│                                      │
│  Cantidad*:                          │
│  ┌────────────────────────────────┐  │
│  │              50                │  │
│  └────────────────────────────────┘  │
│                                      │
│  Notas (opcional):                   │
│  ┌────────────────────────────────┐  │
│  │  Compra a proveedor XYZ        │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │  Stock actual:     45         │  │
│  │  Stock después:    95    ✅   │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │        Registrar               │  │
│  └────────────────────────────────┘  │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (back, title)
- `SingleChoiceSegmentedButtonRow` (entrada/salida toggle)
  - `SegmentedButton` × 2 con `Icons.Filled.ArrowDownward` (entrada) y `ArrowUpward` (salida)
- `SearchableDropdown` (producto selector con stock info)
- `OutlinedTextField` (cantidad, `KeyboardType.Number`)
- `OutlinedTextField` (notas, `minLines = 2`)
- `Card` (stock preview, color-coded: green si válido, red si stock insuficiente)
- `Button` (register, full-width)
- Validación: cantidad > 0, cantidad ≤ stock (solo para salidas)

---

### 2.8 Sale Form (UC14)

```
┌──────────────────────────────────────┐
│  ←  Nueva Venta                      │
├──────────────────────────────────────┤
│                                      │
│  Cliente (opcional):                 │
│  ┌────────────────────────────────┐  │
│  │ 🔍  Buscar cliente...         │  │
│  └────────────────────────────────┘  │
│                                      │
│  Items:                              │
│  ┌────────────────────────────────┐  │
│  │ Widget ABC                     │  │
│  │ x5 @ $25,000                   │  │
│  │ Subtotal: $125,000       [🗑]  │  │
│  ├────────────────────────────────┤  │
│  │ Cable HDMI 2m                  │  │
│  │ x10 @ $15,000                  │  │
│  │ Subtotal: $150,000       [🗑]  │  │
│  └────────────────────────────────┘  │
│                                      │
│  + Agregar producto                  │
│                                      │
│  Notas:                              │
│  ┌────────────────────────────────┐  │
│  │  Venta al por mayor            │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │  Total: $275,000               │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │      Registrar Venta           │  │
│  └────────────────────────────────┘  │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (back, title)
- `SearchableDropdown` (cliente selector)
- `LazyColumn` (sale items)
  - `SaleItemCard` (producto, qty, price, subtotal, remove button)
- `TextButton` ("+ Agregar producto")
- `OutlinedTextField` (notas)
- `Card` (total, Headline Medium, `Color.primary`)
- `Button` (register, full-width)
- Total calculado: sum(subtotals)

---

### 2.9 Settings (UC22/UC23)

```
┌──────────────────────────────────────┐
│  ←  Configuración                    │
├──────────────────────────────────────┤
│                                      │
│  Cuenta                              │
│  ┌────────────────────────────────┐  │
│  │  👤  Juan Pérez               │  │
│  │      juan@empresa.com          │  │
│  │      Rol: Admin                │  │
│  └────────────────────────────────┘  │
│                                      │
│  Workspace                           │
│  ┌────────────────────────────────┐  │
│  │  📋  Bodega Principal          │  │
│  │      Cambiar workspace →       │  │
│  └────────────────────────────────┘  │
│                                      │
│  Apariencia                          │
│  ┌────────────────────────────────┐  │
│  │  🌙  Modo oscuro        [⚙]  │  │
│  └────────────────────────────────┘  │
│                                      │
│  Acerca de                           │
│  ┌────────────────────────────────┐  │
│  │  Versión 1.0.0                 │  │
│  └────────────────────────────────┘  │
│                                      │
│                                      │
│  ┌────────────────────────────────┐  │
│  │       Cerrar Sesión            │  │
│  └────────────────────────────────┘  │
│                                      │
└──────────────────────────────────────┘
```

**Componentes Compose:**
- `TopAppBar` (back, title)
- `LazyColumn`
  - `ProfileCard` (avatar, name, email, role badge)
  - `ListItem` (workspace, trailing: chevron)
  - `Switch` (dark mode toggle)
  - `ListItem` (version)
  - `Button` (logout, outlined, `MaterialTheme.colorScheme.error`)

---

### 2.10 Bottom Navigation Bar

```
┌──────────────────────────────────────┐
│                                      │
│          (contenido)                 │
│                                      │
├──────────────────────────────────────┤
│                                      │
│  🏠        📦        ➕        📋     ⚙️  │
│ Home    Productos   Nuevo    Movim.  Conf│
│  ●                                    │
│                                      │
└──────────────────────────────────────┘

Items:
1. Home         Icon: Home              Route: "home"
2. Productos    Icon: Inventory2        Route: "productos"
3. Nuevo (FAB)  Icon: Add              Action: show BottomSheet
4. Movimientos  Icon: SwapHoriz        Route: "movimientos"
5. Settings     Icon: Settings         Route: "settings"
```

**FAB Bottom Sheet (al tocar "+"):**
```
┌──────────────────────────────────────┐
│  ─────────                           │
│                                      │
│  + Nuevo Producto                    │
│  ┌────────────────────────────────┐  │
│  │  📦  Crear producto nuevo      │  │
│  └────────────────────────────────┘  │
│                                      │
│  + Nuevo Movimiento                  │
│  ┌────────────────────────────────┐  │
│  │  🔄  Registrar entrada o salida│  │
│  └────────────────────────────────┘  │
│                                      │
│  + Nueva Venta                       │
│  ┌────────────────────────────────┐  │
│  │  💰  Crear venta               │  │
│  └────────────────────────────────┘  │
│                                      │
└──────────────────────────────────────┘
```
