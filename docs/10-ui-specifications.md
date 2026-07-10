# UI Specifications — InventarioApp Android

**Mockups de alta fidelidad por pantalla con especificaciones exactas de layout, espaciado y comportamiento.**

---

## 1. Login Screen

```
┌──────────────────────────────────────────┐
│              (status bar)                │
│              color: background           │
├──────────────────────────────────────────┤
│                                          │
│                                          │
│                                          │
│              📦                          │
│           (64dp, Primary)                │
│                                          │
│         InventarioApp                    │
│      Headline Large / Medium             │
│      color: OnBackground                 │
│                                          │
│    Sistema de Inventarios                │
│      Body Medium                         │
│      color: OnSurfaceVariant             │
│                                          │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ Email                            │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField                       │
│  KeyboardType: Email                     │
│  LeadingIcon: Email (24dp)               │
│  width: matchParent - 32dp (16dp each)   │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ Contraseña                  👁   │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField                       │
│  VisualTransformation: Password          │
│  TrailingIcon: Visibility toggle         │
│  width: matchParent - 32dp               │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │        Iniciar Sesión            │    │
│  └──────────────────────────────────┘    │
│  Button (Filled)                         │
│  width: matchParent - 32dp               │
│  enabled: !isLoading && fields not empty  │
│  Loading: CircularProgressIndicator       │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│        ¿Olvidaste contraseña?            │
│        TextButton                        │
│        color: Primary                    │
│        Body Medium                       │
│                                          │
│                                          │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Keyboard: email first, then password
- Enter on email → focus password
- Enter on password → submit login
- Loading state: button shows CircularProgressIndicator
- Error: Snackbar with error message
- Success: navigate to OrgSelector, clear back stack
```

---

## 2. Organization Selector

```
┌──────────────────────────────────────────┐
│              (status bar)                │
├──────────────────────────────────────────┤
│                                          │
│                                          │
│              📦                          │
│           (64dp, Primary)                │
│                                          │
│         InventarioApp                    │
│      Headline Medium / Medium            │
│                                          │
│                                          │
│  Selecciona tu empresa:                  │
│  Body Large                              │
│  color: OnSurfaceVariant                 │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ 🏢  Mi Empresa S.A.             │    │
│  │     3 workspaces · Plan FREE    │    │
│  └──────────────────────────────────┘    │
│  Card (Elevated)                         │
│  padding: 16dp                           │
│  onClick → navigate to WsSelector        │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ 🏢  Otra Empresa Ltda           │    │
│  │     1 workspace · Plan PRO      │    │
│  └──────────────────────────────────┘    │
│  Card (Elevated)                         │
│  onClick → navigate to WsSelector        │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ 🏢  Startup Tech               │    │
│  │     5 workspaces · Plan FREE    │    │
│  └──────────────────────────────────┘    │
│  Card (Elevated)                         │
│  onClick → navigate to WsSelector        │
│                                          │
│                                          │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- List scrollable if >3 orgs
- Loading: CircularProgressIndicator centered
- Empty: "No tienes organizaciones" message
- If only 1 org → skip this screen automatically
- Each card: icon (Business, 24dp, Primary) + name + count + plan badge
```

---

## 3. Workspace Selector

```
┌──────────────────────────────────────────┐
│ ← Mi Empresa S.A.                       │
├──────────────────────────────────────────┤
│                                          │
│  Selecciona workspace:                   │
│  Body Large                              │
│  color: OnSurfaceVariant                 │
│  padding: 16dp horizontal, 24dp top      │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  ┌════════════════════════════════════┐  │
│  │  📋  Bodega Principal              │  │
│  │      142 productos                 │  │
│  │      Tu rol: [Admin]              │  │
│  └════════════════════════════════════┘  │
│  Card (Elevated, selected state)         │
│  Fondo: Surface Container                │
│  onClick → save + navigate Home          │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  📋  Sucursal Norte              │    │
│  │      89 productos                │    │
│  │      Tu rol: [Editor]           │    │
│  └──────────────────────────────────┘    │
│  Card (Elevated)                         │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  📋  Almacén Central             │    │
│  │      256 productos               │    │
│  │      Tu rol: [Viewer]           │    │
│  └──────────────────────────────────┘    │
│  Card (Elevated)                         │
│                                          │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- TopAppBar: back arrow + org name
- Each card: FolderOpen icon (24dp, Primary) + name + count + role badge
- Role badge colors: Admin=PrimaryContainer, Editor=SecondaryContainer, Viewer=SurfaceVariant
- Selected card: Surface Container background
- Click → save orgSlug + wsSlug + wsId to DataStore → navigate Home
- Loading: CircularProgressIndicator centered
```

---

## 4. Home / Dashboard

```
┌──────────────────────────────────────────┐
│ ☰  Bodega Principal                🔔 3 │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  ┌──────────────────┬──────────────────┐ │
│  │  📦              │  📊              │ │
│  │  142             │  3,850           │ │
│  │  Productos       │  Unidades        │ │
│  └──────────────────┴──────────────────┘ │
│  LazyVerticalGrid(Columns = Fixed(2))    │
│  spacing: 8dp                            │
│  MetricCard × 4                          │
│                                          │
│  ┌──────────────────┬──────────────────┐ │
│  │  ⚠️              │  💰              │ │
│  │  8               │  $12.5M          │ │
│  │  Stock Bajo      │  Valor Inventario│ │
│  └──────────────────┴──────────────────┘ │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  Últimos Movimientos                     │
│  Title Medium                            │
│  padding: 16dp horizontal                │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  ↑  Entrada         x50         │    │
│  │     Widget ABC                  │    │
│  │     Juan Pérez · 10:30 a.m.    │    │
│  ├──────────────────────────────────┤    │
│  │  ↓  Salida          x10         │    │
│  │     Cable HDMI 2m              │    │
│  │     María López · 9:15 a.m.    │    │
│  ├──────────────────────────────────┤    │
│  │  ↑  Entrada         x200        │    │
│  │     Mouse Inalámbrico          │    │
│  │     Carlos Ruiz · 8:00 a.m.    │    │
│  └──────────────────────────────────┘    │
│  LazyColumn of MovementItem              │
│  maxItems: 10                            │
│                                          │
├──────────────────────────────────────────┤
│  🏠      📦      ➕      📋     ⚙️       │
│  ●                                       │
└──────────────────────────────────────────┘

Behavior:
- Hamburger menu: opens NavigationDrawer (not implemented yet)
- Notification bell: badge with count, tap → notifications list
- MetricCards: tap → navigate to filtered list (e.g., tap "Stock Bajo" → productos with low stock)
- MovementItem: tap → movement detail (future)
- Pull to refresh: reloads dashboard data
- Loading: shimmer placeholders for metric cards
- Empty movements: "Sin movimientos recientes" message
```

### MetricCard Specification

```
┌──────────────────────────────────┐
│  📦                              │  Icon: 24dp
│  142                             │  Headline Medium, bold
│  Productos                       │  Body Small, OnSurfaceVariant
└──────────────────────────────────┘

Internal padding: 16dp
Icon background: PrimaryContainer circle (40dp)
Card height: 80dp
Radio: 12dp
Elevation: 1dp
```

---

## 5. Product List

```
┌──────────────────────────────────────────┐
│ ← Productos                       🔍  ⋮ │
├──────────────────────────────────────────┤
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ 🔍  Buscar producto...          │    │
│  └──────────────────────────────────┘    │
│  SearchBar (expandable)                  │
│  width: matchParent - 32dp               │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  Filtrar: [Todos ▼]                      │
│  FilterChip row                          │
│  padding: 16dp horizontal                │
│                                          │
│  ─── 8dp ───                            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Widget ABC              CAT-001 │    │
│  │  Electrónicos                    │    │
│  │  ─────────────────────────────── │    │
│  │  Stock: 45          $25,000      │    │
│  └──────────────────────────────────┘    │
│  ProductCard                             │
│  padding: 16dp horizontal, 8dp vertical  │
│  onClick → navigate to ProductoForm      │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Cable HDMI 2m         CAT-002  │    │
│  │  Electrónicos          ⚠️       │    │
│  │  ─────────────────────────────── │    │
│  │  Stock: 3           $15,000      │    │
│  └──────────────────────────────────┘    │
│  Low stock: warning icon + red badge     │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Mouse Inalámbrico    CAT-001   │    │
│  │  Electrónicos                   │    │
│  │  ─────────────────────────────── │    │
│  │  Stock: 120         $35,000     │    │
│  └──────────────────────────────────┘    │
│                                          │
│                       ┌──────────────┐   │
│                       │  + Nuevo     │   │
│                       │  Producto    │   │
│                       └──────────────┘   │
│                       ExtendedFAB        │
│                       text + icon        │
│                                          │
├──────────────────────────────────────────┤
│  🏠      📦      ➕      📋     ⚙️       │
│                 ●                         │
└──────────────────────────────────────────┘

Behavior:
- Search: real-time filter by name or code
- Category filter: FilterChip dropdown with all categories
- Pull to refresh
- Swipe to delete (admin only): red background + Delete icon
- Empty state: "No hay productos" + "Crear producto" button
- Loading: shimmer cards (3 placeholders)
- Pagination: infinite scroll ( Paging 3)
- FAB: ExtendedFloatingActionButton with "Nuevo Producto"
```

### ProductCard Specification

```
┌──────────────────────────────────────────┐
│  Widget ABC                      CAT-001 │
│  Electrónicos                            │
│  ─────────────────────────────────────── │
│  Stock: 45              $25,000          │
│  ⚠️                                       │
└──────────────────────────────────────────┘

Row 1: Title Medium (nombre) + Spacer + Label Small (código, OnSurfaceVariant)
Row 2: Body Small (categoría nombre, OnSurfaceVariant)
Divider: inset 0dp, color SurfaceVariant
Row 3: StockBadge (ok/warning/critical) + Spacer + Body Medium bold (precio COP)

Low stock indicator: Warning icon (16dp, Warning color) when stock <= stockMinimo
```

---

## 6. Product Form (Create/Edit)

```
┌──────────────────────────────────────────┐
│ ←  Guardar                              │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Código*:                                │
│  ┌──────────────────────────────────┐    │
│  │ PROD-001                         │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField                       │
│  supportingText: "Ej: Elect-001"         │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Nombre*:                                │
│  ┌──────────────────────────────────┐    │
│  │ Widget ABC                       │    │
│  └──────────────────────────────────┘    │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Descripción:                            │
│  ┌──────────────────────────────────┐    │
│  │ Widget de alta calidad para      │    │
│  │ aplicaciones industriales        │    │
│  │                                  │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField (multiline)           │
│  minLines: 2, maxLines: 4               │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Categoría*:                             │
│  ┌──────────────────────────────────┐    │
│  │ Electrónicos                ▼   │    │
│  └──────────────────────────────────┘    │
│  ExposedDropdownMenuBox                  │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Precio*:                 Stock Mínimo:  │
│  ┌────────────────┐ ┌────────────────┐   │
│  │ $ 25,000       │ │      10        │   │
│  └────────────────┘ └────────────────┘   │
│  Row: 2 OutlinedTextField                │
│  Precio: KeyboardType.Number             │
│  StockMínimo: KeyboardType.Number        │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Stock Actual:                           │
│  ┌──────────────────────────────────┐    │
│  │             45                   │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField (disabled/readonly)   │
│  Solo visible en modo edición            │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │           Guardar                │    │
│  └──────────────────────────────────┘    │
│  Button (Filled)                         │
│  width: matchParent - 32dp               │
│  enabled: form.isValid && !isLoading     │
│                                          │
│  ─── 16dp ───                           │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Edit mode: pre-fill all fields, hide stock actual (managed via movements)
- Create mode: stock = 0 (set via movements)
- Validation: codigo required, nombre required, categoria required, precio > 0
- Save button: shows CircularProgressIndicator while saving
- Success: Snackbar "Producto guardado" + navigateBack
- Error: field-level errors + Snackbar
- Back with unsaved changes: confirmation dialog
```

---

## 7. Movement Form

```
┌──────────────────────────────────────────┐
│ ←  Registrar Movimiento                  │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Tipo:                                   │
│  ┌──────────────────┬──────────────────┐ │
│  │  ↓  ENTRADA      │    SALIDA  ↑    │ │
│  └──────────────────┴──────────────────┘ │
│  SingleChoiceSegmentedButtonRow          │
│  width: matchParent - 32dp               │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Producto*:                              │
│  ┌──────────────────────────────────┐    │
│  │ 🔍  Buscar producto...          │    │
│  ├──────────────────────────────────┤    │
│  │  Widget ABC              (45)   │    │
│  │  Cable HDMI 2m           (3) ⚠ │    │
│  │  Mouse Inalámbrico      (120)   │    │
│  └──────────────────────────────────┘    │
│  SearchableDropdown                      │
│  Item: nombre + stock count              │
│  Low stock: warning icon                 │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Cantidad*:                              │
│  ┌──────────────────────────────────┐    │
│  │              50                  │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField                       │
│  KeyboardType: Number                    │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Notas (opcional):                       │
│  ┌──────────────────────────────────┐    │
│  │ Compra a proveedor XYZ           │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField (multiline)           │
│  minLines: 2, maxLines: 4               │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Stock actual:     45           │    │
│  │  Stock después:    95      ✅   │    │
│  └──────────────────────────────────┘    │
│  Card (Filled)                           │
│  Fondo: SuccessContainer                 │
│  Radio: 12dp                             │
│  Validation:                             │
│    - ENTRADA: siempre válido             │
│    - SALIDA: stock después >= 0          │
│    - Si inválido: Fondo ErrorContainer   │
│      "Stock insuficiente"                │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │         Registrar                │    │
│  └──────────────────────────────────┘    │
│  Button (Filled)                         │
│  width: matchParent - 32dp               │
│  enabled: form.isValid && !isLoading     │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Default tipo: ENTRADA (pre-selected)
- Product search: real-time filter
- Stock preview: updates in real-time as quantity changes
- Validation: quantity > 0, quantity <= stock (for salidas only)
- Success: Snackbar "Movimiento registrado" + navigateBack
- Error: Snackbar with error message
```

---

## 8. Sale Form

```
┌──────────────────────────────────────────┐
│ ←  Nueva Venta                           │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Cliente (opcional):                     │
│  ┌──────────────────────────────────┐    │
│  │ 🔍  Buscar cliente...           │    │
│  └──────────────────────────────────┘    │
│  SearchableDropdown                      │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Items:                                  │
│  ┌──────────────────────────────────┐    │
│  │  Widget ABC                      │    │
│  │  x5 @ $25,000                    │    │
│  │  Subtotal: $125,000        [🗑]  │    │
│  ├──────────────────────────────────┤    │
│  │  Cable HDMI 2m                   │    │
│  │  x10 @ $15,000                   │    │
│  │  Subtotal: $150,000        [🗑]  │    │
│  └──────────────────────────────────┘    │
│  LazyColumn of ItemVentaCard             │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  + Agregar producto              │    │
│  └──────────────────────────────────┘    │
│  TextButton + Add icon                   │
│  Opens product selector dialog           │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Notas:                                  │
│  ┌──────────────────────────────────┐    │
│  │ Venta al por mayor               │    │
│  └──────────────────────────────────┘    │
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Total: $275,000                 │    │
│  └──────────────────────────────────┘    │
│  Card (Filled)                           │
│  Fondo: PrimaryContainer                 │
│  Total: Headline Medium, bold            │
│  Number formatting: COP with dots        │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │       Registrar Venta            │    │
│  └──────────────────────────────────┘    │
│  Button (Filled)                         │
│  enabled: items.isNotEmpty && !loading   │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Add product: opens dialog with product selector + quantity input
- Remove item: trash icon, confirmation if quantity > 1
- Total: auto-calculated, updates in real-time
- Validation: at least 1 item, quantity > 0 for each
- Success: Snackbar "Venta registrada" + navigateBack
- Stock deducted transactionally on backend
```

### ItemVentaCard Specification

```
┌──────────────────────────────────────────┐
│  Widget ABC                              │
│  x5 @ $25,000                            │
│  Subtotal: $125,000               [🗑]   │
└──────────────────────────────────────────┘

Row 1: Body Large (producto nombre)
Row 2: Body Medium "x{cantidad} @ ${precio}" OnSurfaceVariant
Row 3: Body Medium bold (subtotal) + Spacer + IconButton(Delete)
Padding: 12dp
Divider at bottom
```

---

## 9. Client Form

```
┌──────────────────────────────────────────┐
│ ←  Guardar                              │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Nombre*:                                │
│  ┌──────────────────────────────────┐    │
│  │ Juan Carlos Pérez                │    │
│  └──────────────────────────────────┘    │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Documento:                              │
│  ┌──────────────────────────────────┐    │
│  │ 1.234.567.890                    │    │
│  └──────────────────────────────────┘    │
│  supportingText: "Cédula o NIT"          │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Email:                                  │
│  ┌──────────────────────────────────┐    │
│  │ juan@empresa.com                 │    │
│  └──────────────────────────────────┘    │
│  KeyboardType: Email                     │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Teléfono:                               │
│  ┌──────────────────────────────────┐    │
│  │ 310-1234567                      │    │
│  └──────────────────────────────────┘    │
│  KeyboardType: Phone                     │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Dirección:                              │
│  ┌──────────────────────────────────┐    │
│  │ Calle 45 # 67-89                 │    │
│  └──────────────────────────────────┘    │
│                                          │
│  ─── 12dp ───                           │
│                                          │
│  Notas:                                  │
│  ┌──────────────────────────────────┐    │
│  │ Cliente frecuente                │    │
│  └──────────────────────────────────┘    │
│  OutlinedTextField (multiline)           │
│  minLines: 2                             │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │           Guardar                │    │
│  └──────────────────────────────────┘    │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Create/Edit: same form, pre-fill in edit mode
- Validation: nombre required, documento unique (backend)
- Success: Snackbar + navigateBack
```

---

## 10. Settings

```
┌──────────────────────────────────────────┐
│ ←  Configuración                         │
├──────────────────────────────────────────┤
│                                          │
│  ─── 16dp ───                           │
│                                          │
│  Cuenta                                  │
│  Label Large / Primary color             │
│  padding: 16dp horizontal                │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  👤  Juan Carlos Pérez           │    │
│  │      juan@empresa.com            │    │
│  │      Rol: Admin                  │    │
│  └──────────────────────────────────┘    │
│  Card (Filled)                           │
│  Avatar: circle 48dp, PrimaryContainer   │
│  Initial letter: OnPrimaryContainer      │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  Workspace                               │
│  Label Large / Primary                   │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  📋  Bodega Principal      →     │    │
│  │      Cambiar workspace           │    │
│  └──────────────────────────────────┘    │
│  ListItem                                │
│  Leading: Icon(FolderOpen)               │
│  Trailing: Icon(ChevronRight)            │
│  onClick → navigate to WsSelector        │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  Apariencia                              │
│  Label Large / Primary                   │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  🌙  Modo oscuro          [⚙]   │    │
│  └──────────────────────────────────┘    │
│  ListItem                                │
│  Leading: Icon(DarkMode)                 │
│  Trailing: Switch                        │
│  onCheckedChanged → toggle theme         │
│                                          │
│  ─── 24dp ───                           │
│                                          │
│  Acerca de                               │
│  Label Large / Primary                   │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  Versión 1.0.0                   │    │
│  └──────────────────────────────────┘    │
│  ListItem                                │
│  Trailing: Body Small (version)          │
│                                          │
│                                          │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │        Cerrar Sesión             │    │
│  └──────────────────────────────────┘    │
│  Button (Outlined)                       │
│  Color: Error                            │
│  width: matchParent - 32dp               │
│  onClick → logout + clear DataStore      │
│                                          │
└──────────────────────────────────────────┘

Behavior:
- Profile card: read-only (edit profile is future feature)
- Workspace: navigates to workspace selector
- Dark mode: toggle saves to DataStore, applies immediately
- Logout: confirmation dialog, then clear + navigate to login
```

---

## 11. Movement List

```
┌──────────────────────────────────────────┐
│ ←  Movimientos                    🔍     │
├──────────────────────────────────────────┤
│                                          │
│  ┌──────────────────────────────────┐    │
│  │ 🔍  Buscar movimiento...        │    │
│  └──────────────────────────────────┘    │
│                                          │
│  [Todos]  [Entradas]  [Salidas]          │
│  FilterChip row                          │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  ↑  Entrada         x50         │    │
│  │     Widget ABC                  │    │
│  │     Juan Pérez · 10:30 a.m.    │    │
│  ├──────────────────────────────────┤    │
│  │  ↓  Salida          x10         │    │
│  │     Cable HDMI 2m              │    │
│  │     María López · 9:15 a.m.    │    │
│  └──────────────────────────────────┘    │
│  LazyColumn                              │
│                                          │
│                       ┌──────────────┐   │
│                       │  + Registrar │   │
│                       └──────────────┘   │
│                       ExtendedFAB        │
│                       onClick → show     │
│                       tipo selector      │
│                                          │
├──────────────────────────────────────────┤
│  🏠      📦      ➕      📋     ⚙️       │
│                       ●                   │
└──────────────────────────────────────────┘

Behavior:
- Filter: Todos/Entradas/Salidas (FilterChip)
- FAB: shows BottomSheet to select tipo (Entrada/Salida) before navigating
- Pull to refresh
- Infinite scroll pagination
- Empty state: "Sin movimientos" + "Registrar movimiento" button
```

---

## 12. Empty States (Todos los screens)

```
┌──────────────────────────────────────────┐
│                                          │
│                                          │
│              📦                          │
│           (64dp, 38% OnSurface)          │
│                                          │
│       No hay productos                   │
│       Title Medium                       │
│       color: OnSurface                   │
│                                          │
│  Crea tu primer producto para            │
│  comenzar a gestionar tu inventario      │
│       Body Medium                        │
│       color: OnSurfaceVariant            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │    +  Nuevo Producto             │    │
│  └──────────────────────────────────┘    │
│  FilledTonalButton                       │
│  o ExtendedFAB                           │
│                                          │
│                                          │
└──────────────────────────────────────────┘
```

---

## 13. Error States

```
┌──────────────────────────────────────────┐
│                                          │
│                                          │
│              ⚠️                          │
│           (64dp, Error color)            │
│                                          │
│       Algo salió mal                     │
│       Title Medium                       │
│       color: Error                       │
│                                          │
│  No pudimos cargar los productos         │
│  Verifica tu conexión e intenta          │
│  de nuevo                                 │
│       Body Medium                        │
│       color: OnSurfaceVariant            │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │       Reintentar                 │    │
│  └──────────────────────────────────┘    │
│  FilledButton                            │
│  onClick → retry load                    │
│                                          │
└──────────────────────────────────────────┘
```
