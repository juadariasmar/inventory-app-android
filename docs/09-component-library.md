# Component Library — InventarioApp Android

**Catálogo de componentes reutilizables con variantes, estados y especificaciones.**

---

## 1. Navegación

### 1.1 TopAppBar

```
┌─────────────────────────────────────────────────────────────────────┐
│                    TOP APP BAR                                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  VARIANTE: Small (default)                                         │
│  ┌──────────────────────────────────────────────────────┐          │
│  │ ←  Título de la Pantalla                    🔍  ⋮   │          │
│  └──────────────────────────────────────────────────────┘          │
│  Alto: 64dp | Padding: 4dp trailing                                │
│                                                                     │
│  VARIANTE: Medium                                                  │
│  ┌──────────────────────────────────────────────────────┐          │
│  │ ←  Título Largo de la                              │          │
│  │    Pantalla                            🔍  ⋮         │          │
│  └──────────────────────────────────────────────────────┘          │
│  Alto: 88dp | Título en 2 líneas                                  │
│                                                                     │
│  VARIANTE: Large                                                   │
│  ┌──────────────────────────────────────────────────────┐          │
│  │                                                      │          │
│  │ ←  Título Muy Largo de la Pantalla                  │          │
│  │    Subtítulo descriptivo                             │          │
│  │    🔍  ⋮                                             │          │
│  └──────────────────────────────────────────────────────┘          │
│  Alto: 128dp | Con subtítulo                                       │
│                                                                     │
│  ESTADOS:                                                           │
│  - Scrolled: elevation = 3dp, fondo = Surface Container            │
│  - Top: elevation = 0dp, fondo = Surface                           │
│                                                                     │
│  COMPOSABLE:                                                        │
│  TopAppBar(                                                        │
│      title = { Text("Título") },                                   │
│      navigationIcon = { IconButton(onClick) { Icon(Back) } },     │
│      actions = { IconButton { Icon(Search) } }                     │
│  )                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.2 BottomNavigationBar

```
┌─────────────────────────────────────────────────────────────────────┐
│                    BOTTOM NAVIGATION BAR                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │  🏠        📦        ➕        📋        ⚙️               │      │
│  │ Home    Productos   Nuevo    Movim.    Config            │      │
│  │  ●                                                        │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                     │
│  Alto: 80dp | 5 items max                                          │
│                                                                     │
│  ITEM STATES:                                                       │
│  - Selected:   Icon = filled, Label = visible, color = Primary     │
│  - Unselected: Icon = outlined, Label = visible, color = OnSurface │
│                                                                     │
│  ITEM STRUCTURE:                                                    │
│  ┌────────────┐                                                    │
│  │    Icon    │  24dp                                               │
│  │   (24dp)   │                                                    │
│  │   Label    │  Label Medium (12sp)                                │
│  └────────────┘                                                    │
│  Alto item: 64dp | Indicador activo: 32dp wide pill                │
│                                                                     │
│  COMPOSABLE:                                                        │
│  NavigationBar {                                                   │
│      NavigationBarItem(                                            │
│          icon = { Icon(Icons.Filled.Home, null) },                 │
│          label = { Text("Home") },                                 │
│          selected = currentRoute == "home",                        │
│          onClick = { navigate("home") }                            │
│      )                                                             │
│  }                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.3 NavigationRail (tablets/landscape)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    NAVIGATION RAIL                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────┐ ┌──────────────────────────────────────────────────┐    │
│  │  🏠  │ │                                                  │    │
│  │ Home │ │              Contenido de pantalla               │    │
│  │  ●   │ │                                                  │    │
│  │      │ │                                                  │    │
│  │  📦  │ │                                                  │    │
│  │Prod. │ │                                                  │    │
│  │      │ │                                                  │    │
│  │  ➕  │ │                                                  │    │
│  │Nuevo │ │                                                  │    │
│  │      │ │                                                  │    │
│  │  📋  │ │                                                  │    │
│  │Movim.│ │                                                  │    │
│  │      │ │                                                  │    │
│  │  ⚙️  │ │                                                  │    │
│  │Config│ │                                                  │    │
│  └──────┘ └──────────────────────────────────────────────────┘    │
│                                                                     │
│  Ancho: 80dp | Items: 3-5                                         │
│  Se muestra en landscape o tablets en lugar de BottomNav           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. Botones

### 2.1 Botones por Prioridad

```
┌─────────────────────────────────────────────────────────────────────┐
│                    BOTONES - JERARQUÍA                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. FILLED (Prioridad alta - 1 por pantalla)                       │
│  ┌────────────────────────────────────┐                            │
│  │         Iniciar Sesión            │                            │
│  └────────────────────────────────────┘                            │
│  Alto: 40dp | Padding H: 24dp | Radio: 20dp (full)                │
│  Fondo: Primary | Texto: OnPrimary | Label Large                   │
│                                                                     │
│  2. FILLED TONAL (Prioridad media)                                 │
│  ┌────────────────────────────────────┐                            │
│  │         Guardar Cambios           │                            │
│  └────────────────────────────────────┘                            │
│  Alto: 40dp | Fondo: Secondary Container                          │
│  Texto: On Secondary Container                                     │
│                                                                     │
│  3. OUTLINED (Prioridad baja)                                      │
│  ┌────────────────────────────────────┐                            │
│  │         Cancelar                  │                            │
│  └────────────────────────────────────┘                            │
│  Alto: 40dp | Borde: 1dp Outline | Fondo: transparent             │
│                                                                     │
│  4. TEXT (Acciones secundarias)                                    │
│  ┌──────────────────┐                                              │
│  │  Ver más         │                                              │
│  └──────────────────┘                                              │
│  Alto: 40dp | Sin fondo/borde | Texto: Primary                     │
│                                                                     │
│  5. ICON BUTTON (Acciones compactas)                               │
│  ┌──────┐                                                          │
│  │  🔍  │                                                          │
│  └──────┘                                                          │
│  Alto: 40dp | Icono: 24dp | Radio: 20dp (full)                    │
│                                                                     │
│  6. FAB (Acción principal de pantalla)                             │
│  ┌──────────┐                                                      │
│  │    +     │                                                      │
│  └──────────┘                                                      │
│  Regular: 56dp × 56dp | Extended: 56dp × auto                     │
│  Icon: 24dp | Fondo: Primary Container                             │
│  Radio: 16dp (Medium)                                              │
│                                                                     │
│  7. SMALL FAB                                                      │
│  ┌──────┐                                                          │
│  │  ✏️  │                                                          │
│  └──────┘                                                          │
│  40dp × 40dp | Icon: 24dp | Radio: 12dp                           │
│                                                                     │
│  8. BOTTOM SHEET FAB (Centro de BottomNav)                         │
│  ┌──────────┐                                                      │
│  │    ➕    │                                                      │
│  └──────────┘                                                      │
│  56dp × 56dp | Fondo: Primary | Icon: 24dp OnPrimary              │
│  Radio: 16dp | Elevación: 3dp                                      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.2 Estados del Botón

```
┌─────────────────────────────────────────────────────────────────────┐
│                    ESTADOS DEL BOTÓN                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ENABLED (normal)                                                  │
│  ┌────────────────────────────────────┐                            │
│  │         Iniciar Sesión            │  Fondo: Primary             │
│  └────────────────────────────────────┘  Texto: OnPrimary          │
│                                                                     │
│  HOVERED / PRESSED                                                 │
│  ┌────────────────────────────────────┐                            │
│  │░░░░░░░ Iniciar Sesión ░░░░░░░░░░░░│  Overlay: 8% Primary      │
│  └────────────────────────────────────┘                            │
│                                                                     │
│  DISABLED                                                          │
│  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐                            │
│  │    Iniciar Sesión (deshabilitado)  │  Fondo: 12% OnSurface    │
│  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘  Texto: 38% OnSurface    │
│                                                                     │
│  LOADING                                                           │
│  ┌────────────────────────────────────┐                            │
│  │  ◌  Iniciar Sesión               │  CircularProgressIndicator │
│  └────────────────────────────────────┘  Texto: OnPrimary          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Campos de Texto

### 3.1 OutlinedTextField

```
┌─────────────────────────────────────────────────────────────────────┐
│                    OUTLINED TEXT FIELD                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  EMPTY (con label flotante)                                        │
│  ┌────────────────────────────────────────┐                        │
│  │ Nombre                                 │                        │
│  └────────────────────────────────────────┘                        │
│  Alto: 56dp | Borde: 1dp Outline | Radio: 4dp                     │
│                                                                     │
│  FILLED                                                            │
│  ┌────────────────────────────────────────┐                        │
│  │ Nombre                                 │                        │
│  │ Widget ABC                             │                        │
│  └────────────────────────────────────────┘                        │
│  Label flotante arriba (12sp, Primary)                             │
│  Texto: Body Large (16sp, OnSurface)                               │
│                                                                     │
│  FOCUSED                                                           │
│  ┌══════════════════════════════════════════┐                      │
│  │ Nombre                                   │                      │
│  │ Widget ABC                               │                      │
│  └══════════════════════════════════════════┘                      │
│  Borde: 2dp Primary | Cursor: Primary                              │
│                                                                     │
│  ERROR                                                             │
│  ┌────────────────────────────────────────┐                        │
│  │ Nombre                                 │                        │
│  │ Widget                                 │                        │
│  └────────────────────────────────────────┘                        │
│  Borde: 2dp Error | Helper: "Campo requerido" (Body Small, Error)  │
│                                                                     │
│  WITH LEADING ICON                                                 │
│  ┌────────────────────────────────────────┐                        │
│  │ 🔍  Buscar producto...                │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  WITH TRAILING ICON (password toggle)                              │
│  ┌────────────────────────────────────────┐                        │
│  │ Contraseña                      👁     │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  COMPOSABLE:                                                        │
│  OutlinedTextField(                                                │
│      value = text,                                                 │
│      onValueChange = onTextChange,                                 │
│      label = { Text("Nombre") },                                  │
│      leadingIcon = { Icon(Icons.Outlined.Search, null) },         │
│      trailingIcon = { Icon(Icons.Outlined.Visibility, null) },    │
│      isError = hasError,                                           │
│      supportingText = if (hasError) {                             │
│          { Text("Campo requerido", color = MaterialTheme.colorScheme.error) }
│      } else null,                                                 │
│      modifier = Modifier.fillMaxWidth()                            │
│  )                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 3.2 TextField para Notas (multiline)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    MULTILINE TEXT FIELD                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │ Notas                                  │                        │
│  │ Compra a proveedor XYZ, entrega        │                        │
│  │ programada para el viernes             │                        │
│  │                                        │                        │
│  └────────────────────────────────────────┘                        │
│  Alto: auto (minLines = 3, maxLines = 6)                           │
│  Align: top                                                        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 4. Cards

### 4.1 Tipos de Card

```
┌─────────────────────────────────────────────────────────────────────┐
│                    TIPOS DE CARD                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. ELEVATED CARD (Default)                                        │
│  ┌────────────────────────────────────────┐                        │
│  │  Título de la Card                     │  Elevación: 1dp        │
│  │  Subtítulo o descripción               │  Fondo: Surface        │
│  │  Contenido adicional                   │  Radio: 12dp           │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  2. FILLED CARD                                                    │
│  ┌══════════════════════════════════════════┐                      │
│  │  Título de la Card                       │  Fondo:              │
│  │  Contenido                               │  Surface Container   │
│  └══════════════════════════════════════════┘  Elevación: 0dp      │
│                                                                     │
│  3. OUTLINED CARD                                                  │
│  ┌────────────────────────────────────────┐                        │
│  │  Título de la Card                     │  Borde: 1dp Outline   │
│  │  Contenido                             │  Fondo: Surface       │
│  └────────────────────────────────────────┘  Elevación: 0dp       │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.2 Cards del Proyecto

```
┌─────────────────────────────────────────────────────────────────────┐
│                    CARDS ESPECÍFICAS                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  METRIC CARD (Dashboard)                                           │
│  ┌────────────────────────────────────────┐                        │
│  │  📦                                    │  Alto: 80dp            │
│  │  Productos            142              │  Radio: 12dp           │
│  └────────────────────────────────────────┘  Elevación: 1dp       │
│  Icon: 24dp Primary Container                                     │
│  Value: Headline Medium, bold                                      │
│  Label: Body Small, OnSurfaceVariant                               │
│                                                                     │
│  PRODUCT CARD (Lista de productos)                                 │
│  ┌────────────────────────────────────────┐                        │
│  │  Widget ABC                  CAT-001   │  Alto: auto (80dp min) │
│  │  Electrónicos                          │  Radio: 12dp           │
│  │  ───────────────────────────────────── │                        │
│  │  Stock: 45              $25,000        │                        │
│  │  ⚠️                                    │                        │
│  └────────────────────────────────────────┘                        │
│  Padding: 16dp                                                     │
│  Row 1: Title Medium (nombre) + Label Small (código)               │
│  Row 2: Body Small (categoría)                                     │
│  Row 3: StockBadge + Text(precio, Body Medium, bold)              │
│  Low stock indicator: Warning icon if stock <= stockMinimo        │
│                                                                     │
│  MOVEMENT CARD                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │  ↑  Entrada           x50              │  Radio: 12dp           │
│  │  Widget ABC                           │                        │
│  │  Juan Pérez · 10:30 a.m.              │                        │
│  └────────────────────────────────────────┘                        │
│  Leading: Icon(tipo, color: entrada=Success, salida=Error)        │
│  Title: Label Large (tipo + cantidad)                              │
│  Subtitle: Body Medium (producto nombre)                           │
│  Caption: Body Small (usuario + hora)                              │
│                                                                     │
│  CATEGORY CARD                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │  Electrónicos              Elect-01    │  Alto: 72dp            │
│  │  142 productos                         │  Radio: 12dp           │
│  └────────────────────────────────────────┘                        │
│  Row: Title (nombre) + Badge (prefijo)                            │
│  Subtitle: Body Small (conteo productos)                           │
│                                                                     │
│  CLIENT CARD                                                       │
│  ┌────────────────────────────────────────┐                        │
│  │  👤  Juan Carlos Pérez                 │  Alto: auto            │
│  │      CC: 1.234.567.890                 │  Radio: 12dp           │
│  │      juan@empresa.com · 310-1234567   │                        │
│  └────────────────────────────────────────┘                        │
│  Leading: Avatar (inicial del nombre, fondo PrimaryContainer)     │
│  Title: Body Large (nombre)                                        │
│  Subtitle: Body Small (documento, email, teléfono)                │
│                                                                     │
│  SALE CARD                                                         │
│  ┌────────────────────────────────────────┐                        │
│  │  Venta #142                            │  Radio: 12dp           │
│  │  Juan Pérez · 3 items                  │                        │
│  │  Total: $275,000            COMPLETADA │                        │
│  └────────────────────────────────────────┘                        │
│  Title: Label Large (#id + fecha)                                  │
│  Subtitle: Body Small (vendedor + items count)                     │
│  Row trailing: Total + Badge(estado, color-coded)                 │
│                                                                     │
│  WORKSPACE CARD (Selector)                                         │
│  ┌══════════════════════════════════════════┐                      │
│  │  📋  Bodega Principal                    │  Fondo:              │
│  │      142 productos                       │  Surface Container   │
│  │      Tu rol: Admin                       │  Radio: 12dp         │
│  └══════════════════════════════════════════┘                      │
│  Leading: Icon(FolderOpen, Primary)                                │
│  Title: Title Medium                                               │
│  Subtitle: Body Small (conteo productos)                           │
│  Trailing: Badge(rol, color-coded)                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 5. Badges y Chips

### 5.1 Badges

```
┌─────────────────────────────────────────────────────────────────────┐
│                    BADGES                                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  STOCK BADGE (en cards de producto)                                │
│  ┌──────────┐                                                      │
│  │ Stock:45 │  Alto: 24dp | Padding H: 8dp                        │
│  └──────────┘  Radio: 4dp (Extra Small)                            │
│                                                                     │
│  ESTADOS:                                                           │
│  - OK (stock > min):    Fondo: SuccessContainer, Text: Success     │
│  - WARNING (stock=min): Fondo: WarningContainer, Text: Warning     │
│  - CRITICAL (stock<min):Fondo: ErrorContainer, Text: Error         │
│                                                                     │
│  ROL BADGE (selector de workspace)                                 │
│  ┌───────┐                                                         │
│  │ Admin │  Alto: 20dp | Padding H: 6dp                           │
│  └───────┘  Radio: 4dp                                             │
│  Colores:                                                           │
│  - Admin:   PrimaryContainer / OnPrimaryContainer                  │
│  - Editor:  SecondaryContainer / OnSecondaryContainer              │
│  - Viewer:  SurfaceVariant / OnSurfaceVariant                      │
│                                                                     │
│  ESTADO VENTA BADGE                                                │
│  ┌────────────┐                                                    │
│  │ COMPLETADA │  Alto: 20dp                                        │
│  └────────────┘  Radio: 4dp                                        │
│  Colores:                                                           │
│  - COMPLETADA: SuccessContainer / Success                          │
│  - PENDIENTE:  WarningContainer / Warning                          │
│  - CANCELADA:  ErrorContainer / Error                              │
│                                                                     │
│  NOTIFICATION BADGE (en TopAppBar)                                 │
│  ┌──┐                                                              │
│  │ 3│  Alto: 16dp | Ancho: 16-24dp (auto)                         │
│  └──┘  Fondo: Error | Texto: OnError | Label Small                 │
│  Posición: top-end del icono de notificación                       │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.2 Chips

```
┌─────────────────────────────────────────────────────────────────────┐
│                    CHIPS                                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  FILTER CHIP (Filtros en listas)                                   │
│  ┌──────────────┐                                                  │
│  │  Todos    ✕  │  Alto: 32dp | Padding H: 12dp                   │
│  └──────────────┘  Radio: 8dp                                      │
│                                                                     │
│  ESTADOS:                                                           │
│  - Selected:   Fondo: SecondaryContainer, Icon: Check              │
│  - Unselected: Borde: 1dp Outline, sin icono                       │
│                                                                     │
│  INPUT CHIP (Selección)                                            │
│  ┌──────────────────┐                                              │
│  │  Electrónicos  ✕ │  Alto: 32dp                                  │
│  └──────────────────┘  Con trailing icon (close)                   │
│                                                                     │
│  ASSIST CHIP                                                       │
│  ┌──────────────────┐                                              │
│  │  🔍  Buscar...   │  Alto: 32dp                                  │
│  └──────────────────┘  Con leading icon                            │
│                                                                     │
│  COMPOSABLE:                                                        │
│  FilterChip(                                                       │
│      selected = selectedFilter == "todos",                         │
│      onClick = { onFilterChanged("todos") },                       │
│      label = { Text("Todos") },                                    │
│      leadingIcon = if (selected) {{                                │
│          Icon(Icons.Filled.Check, null, modifier = Modifier.size(18.dp)) }
│      }}                                                            │
│  )                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. Listas

### 6.1 ListItem

```
┌─────────────────────────────────────────────────────────────────────┐
│                    LIST ITEM                                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ESTRUCTURA BASE:                                                   │
│  ┌────────────────────────────────────────────────────────┐        │
│  │  [Leading]    Headline Content                         │        │
│  │  [Avatar]     Supporting Content                       │        │
│  │  [Icon]       Tertiary Content                         │        │
│  │              [Trailing Content]                         │        │
│  └────────────────────────────────────────────────────────┘        │
│  Alto: 56dp (1 línea) / 72dp (2 líneas) / 88dp (3 líneas)        │
│  Padding: 16dp horizontal                                          │
│                                                                     │
│  VARIANTES EN EL PROYECTO:                                          │
│                                                                     │
│  1. Con avatar (clientes)                                          │
│  ┌────────────────────────────────────────┐                        │
│  │  [👤]  Juan Pérez               →     │                        │
│  │        CC: 1.234.567.890               │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  2. Con icono (categorías)                                         │
│  ┌────────────────────────────────────────┐                        │
│  │  [📦]  Electrónicos            Elect-01│                        │
│  │        142 productos                   │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  3. Con badge (ventas)                                             │
│  ┌────────────────────────────────────────┐                        │
│  │  Venta #142                    COMPLET │                        │
│  │  3 items · $275,000                   │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  4. Selectable (productos en selector)                             │
│  ┌══════════════════════════════════════════┐                      │
│  │  ✓ Widget ABC                     45     │  Fondo seleccionado  │
│  └══════════════════════════════════════════┘  Surface Container   │
│                                                                     │
│  5. Swipe actions (eliminar)                                       │
│  ┌────────────────────────────────────────┐                        │
│  │ ←←← ELIMINAR | Widget ABC        →→→  │  Swipe to reveal       │
│  └────────────────────────────────────────┘                        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Divider

```
┌─────────────────────────────────────────────────────────────────────┐
│                    DIVIDER                                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  INSET DIVIDER (entre items de lista)                              │
│  ────────────────────────────────────────────  Alto: 1dp           │
│  Padding: 16dp start (indentado)                                   │
│  Color: SurfaceVariant                                             │
│                                                                     │
│  FULL DIVIDER (entre secciones)                                    │
│  ────────────────────────────────────────────  Alto: 1dp           │
│  Sin padding, color: SurfaceVariant                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 7. Diálogos

### 7.1 AlertDialog

```
┌─────────────────────────────────────────────────────────────────────┐
│                    ALERT DIALOG                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────────────────────────────┐                  │
│  │                                              │                  │
│  │  Eliminar producto                           │  Título:         │
│  │                                              │  Title Medium    │
│  │  ¿Estás seguro de eliminar Widget ABC?       │                  │
│  │  Esta acción no se puede deshacer.           │  Body:           │
│  │                                              │  Body Medium     │
│  │                                              │                  │
│  │            Cancelar        Eliminar          │  Botones:        │
│  │                                              │  TextButton +    │
│  └──────────────────────────────────────────────┘  TextButton(red) │
│                                                                     │
│  Elevación: 8dp | Radio: 28dp | Padding: 24dp                     │
│  Scrim: #000000 a 32%                                              │
│                                                                     │
│  CONFIRM DELETE DIALOG (reutilizable)                              │
│  - Título: "Eliminar [entidad]"                                    │
│  - Body: "¿Estás seguro de eliminar [nombre]? No se puede..."    │
│  - Cancel: TextButton("Cancelar")                                  │
│  - Confirm: TextButton("Eliminar", color = Error)                  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 7.2 BottomSheet

```
┌─────────────────────────────────────────────────────────────────────┐
│                    MODAL BOTTOM SHEET                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────────────────────────────┐                  │
│  │  ─────────  (drag handle)                    │  Handle: 32dp    │
│  │                                              │  wide, 4dp tall  │
│  │  Opciones                                    │  Radio: 28dp top │
│  │                                              │  Elevación: 6dp  │
│  │  ┌──────────────────────────────────────┐   │                  │
│  │  │  📦  Nuevo Producto                  │   │  ListItem        │
│  │  ├──────────────────────────────────────┤   │                  │
│  │  │  🔄  Nuevo Movimiento                │   │  Divider         │
│  │  ├──────────────────────────────────────┤   │                  │
│  │  │  💰  Nueva Venta                     │   │  ListItem        │
│  │  └──────────────────────────────────────┘   │                  │
│  │                                              │                  │
│  └──────────────────────────────────────────────┘                  │
│                                                                     │
│  Scrim: #000000 a 32%                                              │
│  Max height: 50% de pantalla (o contenido)                         │
│  Swipe down to dismiss                                             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 8. Indicadores

### 8.1 Loading States

```
┌─────────────────────────────────────────────────────────────────────┐
│                    LOADING STATES                                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. CIRCULAR INDICATOR (inline)                                    │
│  ┌────────────────────────────────────────┐                        │
│  │  ◌                                     │  40dp × 40dp          │
│  └────────────────────────────────────────┘  Color: Primary        │
│  Uso: botones loading, pulls to refresh                           │
│                                                                     │
│  2. LOADING OVERLAY (pantalla completa)                            │
│  ┌────────────────────────────────────────┐                        │
│  │                                        │  Fondo: scrim 8%      │
│  │            ◌                           │  Center: 48dp         │
│  │                                        │  CircularProgressIndicator │
│  │                                        │  Color: Primary        │
│  └────────────────────────────────────────┘                        │
│  Uso: carga inicial de datos                                       │
│                                                                     │
│  3. SHIMMER (placeholder en listas)                                │
│  ┌────────────────────────────────────────┐                        │
│  │  ████████████████  ████████████        │  Alto: 80dp            │
│  │  ██████████████████████████████        │  Radio: 12dp           │
│  │  ████████████████  ████████████        │  Fondo: Shimmer        │
│  └────────────────────────────────────────┘  animation             │
│  Uso: skeleton loading de cards                                    │
│                                                                     │
│  4. PULL TO REFRESH                                                │
│  ┌────────────────────────────────────────┐                        │
│  │  ↻                                    │  Circular indicator    │
│  │  (contenido de la lista)              │  en top de lista       │
│  └────────────────────────────────────────┘  SwipeRefreshLayout   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 8.2 Empty States

```
┌─────────────────────────────────────────────────────────────────────┐
│                    EMPTY STATE                                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │                                        │                        │
│  │              📦                        │  Icono: 64dp           │
│  │                                        │  Color: OnSurface      │
│  │        No hay productos                │  Variant (38%)         │
│  │                                        │                        │
│  │  Crea tu primer producto para          │  Título: Title Medium  │
│  │  comenzar a gestionar tu inventario    │  Body: Body Medium     │
│  │                                        │  Color: OnSurface      │
│  │        + Nuevo Producto                │  Variant               │
│  │                                        │                        │
│  │                                        │  Botón: Filled Tonal   │
│  └────────────────────────────────────────┘  o Extended FAB        │
│                                                                     │
│  VARIANTES:                                                         │
│  - Sin datos: icono + título + descripción + acción               │
│  - Sin resultados de búsqueda: icono + "No se encontraron..."     │
│  - Error: icono de error + "Algo salió mal" + retry button        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 9. Snackbar

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SNACKBAR                                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │  Movimiento registrado exitosamente            Deshacer  │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                     │
│  Alto: auto (min 48dp) | Padding: 16dp                             │
│  Fondo:.inverseSurface | Texto:inverseOnSurface                    │
│  Radio: 4dp | Elevación: 6dp                                       │
│  Duración: Short (snackbarDuration = SnackbarDuration.Short)       │
│  Posición: Bottom (above BottomNav if present)                     │
│                                                                     │
│  VARIANTES:                                                         │
│  - Success: "Operación completada"                                 │
│  - Error: "Error al guardar" + retry action                        │
│  - Info: "Sesión expirada, inicia sesión de nuevo"                │
│  - Undo: "Producto eliminado" + Deshacer action                    │
│                                                                     │
│  COMPOSABLE:                                                        │
│  val snackbarHostState = remember { SnackbarHostState() }          │
│  LaunchedEffect(Unit) {                                            │
│      snackbarHostState.showSnackbar(                               │
│          message = "Movimiento registrado",                        │
│          actionLabel = "Deshacer",                                 │
│          duration = SnackbarDuration.Short                         │
│      )                                                             │
│  }                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 10. Toggle / Segmented Button

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SEGMENTED BUTTON                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  SINGLE CHOICE (Tipo de movimiento)                                │
│  ┌──────────────┬──────────────┐                                   │
│  │  ↓ ENTRADA   │    SALIDA ↑  │  Alto: 40dp                      │
│  └──────────────┴──────────────┘  Radio: 20dp (full)               │
│                                                                     │
│  ESTADOS:                                                           │
│  - Selected: Fondo: Secondary Container, Text: OnSecondary         │
│  - Unselected: Borde: 1dp Outline, Text: OnSurface                 │
│                                                                     │
│  COMPOSABLE:                                                        │
│  val options = listOf("Entrada", "Salida")                         │
│  SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
│      options.forEachIndexed { index, label ->                      │
│          SegmentedButton(                                          │
│              shape = SegmentedButtonDefaults.itemShape(            │
│                  index = index,                                     │
│                  count = options.size                               │
│              ),                                                    │
│              onClick = { onTipoChanged(label) },                   │
│              selected = selectedTipo == label,                     │
│              icon = {                                              │
│                  Icon(                                             │
│                      if (label == "Entrada") Icons.Filled.ArrowDownward
│                      else Icons.Filled.ArrowUpward,               │
│                      contentDescription = null,                    │
│                      modifier = Modifier.size(18.dp)               │
│                  )                                                 │
│              }                                                     │
│          ) { Text(label) }                                         │
│      }                                                             │
│  }                                                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 11. Componentes de Selección

### 11.1 ExposedDropdownMenuBox

```
┌─────────────────────────────────────────────────────────────────────┐
│                    DROPDOWN MENU                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │  Categoría                       ▼     │  OutlinedTextField     │
│  └────────────────────────────────────────┘  + trailing icon       │
│  ┌────────────────────────────────────────┐                        │
│  │  Electrónicos                          │  Menu items            │
│  ├────────────────────────────────────────┤  Radio: 4dp            │
│  │  Periféricos                           │  Elevación: 3dp        │
│  ├────────────────────────────────────────┤  Ancho: match TextField│
│  │  Redes                                 │                        │
│  └────────────────────────────────────────┘                        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 11.2 SearchableDropdown

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SEARCHABLE DROPDOWN                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────────────────────────────┐                        │
│  │ 🔍  Buscar producto...                │  Search field          │
│  ├────────────────────────────────────────┤                        │
│  │  Widget ABC              Stock: 45     │  Filtered results      │
│  │  Cable HDMI 2m           Stock: 3  ⚠  │  with stock info       │
│  │  Mouse Inalámbrico       Stock: 120    │  and low stock warning │
│  └────────────────────────────────────────┘                        │
│                                                                     │
│  Alto item: 56dp | Padding: 16dp                                   │
│  Max items visibles: 5 (scrollable)                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```
