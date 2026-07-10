# Design System — InventarioApp Android

**Versión:** 1.0.0
**Base:** Material Design 3 (Material You)
**Framework:** Jetpack Compose + Compose Material3

---

## 1. Principios de Diseño

| Principio | Descripción |
|-----------|------------|
| **Consistencia** | Todos los elementos siguen los mismos tokens y patrones |
| **Claridad** | La información jerárquica es evidente sin esfuerzo cognitivo |
| **Eficiencia** | Las acciones comunes requieren el mínimo número de taps |
| **Accesibilidad** | Cumple WCAG 2.1 AA (contraste 4.5:1 texto, 3:1 gráficos) |
| **Adaptabilidad** | Se adapta a modo claro/oscuro y diferentes tamaños de pantalla |

---

## 2. Paleta de Colores

### 2.1 Modo Claro (Light Theme)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    PALETA PRIMARIA                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Primary          #1B5E20    ████████  Verde oscuro (inventario)   │
│  On Primary       #FFFFFF    ████████  Texto sobre primario        │
│  Primary Container #A5D6A7   ████████  Fondos suaves, badges       │
│  On Primary Container #002204 ████████ Texto sobre container       │
│                                                                     │
│  Secondary        #4A6572    ████████  Azul-gris (acciones sec.)   │
│  On Secondary     #FFFFFF    ████████  Texto sobre secundario      │
│  Secondary Container #C8D8E0 ████████  Chips, tags                 │
│  On Secondary Container #051F2C ████████                           │
│                                                                     │
│  Tertiary         #7C5800    ████████  Dorado (destacados)         │
│  On Tertiary      #FFFFFF    ████████                              │
│  Tertiary Container #FFDEA1  ████████  Badges premium              │
│  On Tertiary Container #271900 ████████                            │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                    PALETA DE ESTADO                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Error            #B71C1C    ████████  Rojo (eliminar, salidas)    │
│  On Error         #FFFFFF    ████████                              │
│  Error Container  #FFCDD2    ████████  Errores suaves              │
│  On Error Container #410002  ████████                              │
│                                                                     │
│  Success          #2E7D32    ████████  Verde (entradas, OK)        │
│  Success Container #C8E6C9   ████████  Confirmaciones              │
│                                                                     │
│  Warning          #F57F17    ████████  Amber (stock bajo)          │
│  Warning Container #FFF8E1   ████████  Alertas suaves              │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                    PALETA NEUTRAL                                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Background       #FAFAFA    ████████  Fondo general               │
│  On Background    #1C1B1F    ████████  Texto principal             │
│                                                                     │
│  Surface          #FFFFFF    ████████  Cards, sheets, dialogs      │
│  On Surface       #1C1B1F    ████████  Texto en surfaces           │
│  Surface Variant  #E7E0EC    ████████  Bordes, divisores           │
│  On Surface Variant #49454F  ████████  Texto secundario            │
│                                                                     │
│  Outline          #79747E    ████████  Bordes de campos            │
│  Outline Variant  #CAC4D0    ████████  Bordes suaves               │
│                                                                     │
│  Surface Container Low     #F7F2FA                                │
│  Surface Container         #F1ECF4                                │
│  Surface Container High    #ECE6F0                                │
│  Surface Container Highest #E6E0E9                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.2 Modo Oscuro (Dark Theme)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    PALETA OSCURA                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Primary          #81C784    ████████  Verde claro                 │
│  On Primary       #003909    ████████                              │
│  Primary Container #1B5E20   ████████  Verde oscuro (container)    │
│  On Primary Container #A5D6A7 ████████                             │
│                                                                     │
│  Secondary        #B0BEC5    ████████  Azul-gris claro             │
│  Secondary Container #37474F ████████                              │
│                                                                     │
│  Background       #121212    ████████  Fondo general               │
│  On Background    #E6E1E5    ████████                              │
│                                                                     │
│  Surface          #1E1E1E    ████████  Cards, sheets               │
│  On Surface       #E6E1E5    ████████                              │
│  Surface Variant  #49454F    ████████                              │
│  On Surface Variant #CAC4D0  ████████                              │
│                                                                     │
│  Outline          #938F99    ████████                              │
│  Outline Variant  #49454F    ████████                              │
│                                                                     │
│  Error            #F2B8B5    ████████  Rojo claro                  │
│  Error Container  #8C1D18    ████████                              │
│                                                                     │
│  Success          #81C784    ████████  Verde claro                 │
│  Warning          #FFB74D    ████████  Amber claro                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.3 Tokens de Color por Uso

| Token | Claro | Oscuro | Uso |
|-------|-------|--------|-----|
| `colorPrimary` | `#1B5E20` | `#81C784` | Botones principales, AppBar, FAB |
| `colorOnPrimary` | `#FFFFFF` | `#003909` | Texto sobre primario |
| `colorPrimaryContainer` | `#A5D6A7` | `#1B5E20` | Badges, chips activos |
| `colorSecondary` | `#4A6572` | `#B0BEC5` | Acciones secundarias |
| `colorError` | `#B71C1C` | `#F2B8B5` | Errores, eliminar |
| `colorSuccess` | `#2E7D32` | `#81C784` | Confirmaciones, entradas |
| `colorWarning` | `#F57F17` | `#FFB74D` | Alertas, stock bajo |
| `colorBackground` | `#FAFAFA` | `#121212` | Fondo de pantalla |
| `colorSurface` | `#FFFFFF` | `#1E1E1E` | Cards, dialogs |
| `colorOnSurface` | `#1C1B1F` | `#E6E1E5` | Texto principal |
| `colorOnSurfaceVariant` | `#49454F` | `#CAC4D0` | Texto secundario |
| `colorOutline` | `#79747E` | `#938F99` | Bordes de campos |

---

## 3. Tipografía

### 3.1 Escala Tipográfica

```
┌─────────────────────────────────────────────────────────────────────┐
│                    ESCALA TIPOGRÁFICA                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Display Large    57sp / 64lh   / Regular   / -0.25sp tracking    │
│  Display Medium   45sp / 52lh   / Regular   / 0sp tracking       │
│  Display Small    36sp / 44lh   / Regular   / 0sp tracking       │
│                                                                     │
│  Headline Large   32sp / 40lh   / Regular   / 0sp tracking       │
│  Headline Medium  28sp / 36lh   / Regular   / 0sp tracking       │
│  Headline Small   24sp / 32lh   / Regular   / 0sp tracking       │
│                                                                     │
│  Title Large      22sp / 28lh   / Medium    / 0sp tracking       │
│  Title Medium     16sp / 24lh   / Medium    / 0.15sp tracking    │
│  Title Small      14sp / 20lh   / Medium    / 0.1sp tracking     │
│                                                                     │
│  Body Large       16sp / 24lh   / Regular   / 0.5sp tracking     │
│  Body Medium      14sp / 20lh   / Regular   / 0.25sp tracking    │
│  Body Small       12sp / 16lh   / Regular   / 0.4sp tracking     │
│                                                                     │
│  Label Large      14sp / 20lh   / Medium    / 0.1sp tracking     │
│  Label Medium     12sp / 16lh   / Medium    / 0.5sp tracking     │
│  Label Small      11sp / 16lh   / Medium    / 0.5sp tracking     │
│                                                                     │
│  (sp = scale Pixels, lh = line height, tracking = letter spacing)  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 3.2 Uso por Componente

| Componente | Estilo | Fuente |
|------------|--------|--------|
| Pantalla título (AppBar) | Title Large | Roboto Medium |
| Sección título | Title Medium | Roboto Medium |
| Card título | Title Medium | Roboto Medium |
| Card subtítulo | Body Medium | Roboto Regular |
| Body texto | Body Large | Roboto Regular |
| Body secundario | Body Medium | Roboto Regular |
| Botón texto | Label Large | Roboto Medium |
| Badge texto | Label Small | Roboto Medium |
| Field label | Label Medium | Roboto Medium |
| Field input | Body Large | Roboto Regular |
| Field helper/error | Body Small | Roboto Regular |
| Navigation label | Label Medium | Roboto Medium |

---

## 4. Espaciado

### 4.1 Grid de Espaciado (base 4dp)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    GRID DE ESPACIADO                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  spacing_xxs    2dp     Entre elementos muy compactos              │
│  spacing_xs     4dp     Entre elementos compactos                  │
│  spacing_sm     8dp     Entre elementos relacionados              │
│  spacing_md     16dp    Padding general de pantalla                │
│  spacing_lg     24dp    Entre secciones                            │
│  spacing_xl     32dp    Separación major                           │
│  spacing_xxl    48dp    Separación entre módulos                   │
│                                                                     │
│  ─── Ejemplo visual ───                                            │
│                                                                     │
│  ┌─── 16dp (spacing_md) ───────────────────────────────┐          │
│  │                                                       │          │
│  │  Título                                               │          │
│  │                                                       │          │
│  │  ─── 8dp (spacing_sm) ───                            │          │
│  │                                                       │          │
│  │  Subtítulo                                            │          │
│  │                                                       │          │
│  │  ─── 24dp (spacing_lg) ───                           │          │
│  │                                                       │          │
│  │  ┌─────────────────────────────────────────────┐     │          │
│  │  │  Card                                       │     │          │
│  │  │  ─── 16dp padding interno ───               │     │          │
│  │  │                                             │     │          │
│  │  │  Contenido de la card                       │     │          │
│  │  │                                             │     │          │
│  │  └─────────────────────────────────────────────┘     │          │
│  │                                                       │          │
│  └───────────────────────────────────────────────────────┘          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.2 Padding de Pantalla

| Zona | Padding | Notas |
|------|---------|-------|
| Horizontal pantalla | 16dp | `contentPadding` en lists |
| Top (sin TopAppBar) | 16dp | Debajo del status bar |
| Top (con TopAppBar) | 0dp | Ya maneja el AppBar |
| Bottom (sin BottomNav) | 16dp | Último elemento |
| Bottom (con BottomNav) | 80dp | Espacio para la barra |
| Between items (lista) | 0dp | Separación por Divider o Card |
| Card interna | 16dp | Padding dentro de cards |

---

## 5. Sombras y Elevación

### 5.1 Niveles de Elevación

```
┌─────────────────────────────────────────────────────────────────────┐
│                    NIVELES DE ELEVACIÓN                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Level 0    0dp      Surface plano (fondo de pantalla)            │
│  Level 1    1dp      Cards en reposo, navigation bars             │
│  Level 2    3dp      Cards elevadas, FAB en reposo                │
│  Level 3    6dp      Menus desplegables, bottom sheets            │
│  Level 4    8dp      Dialogs, drawers                              │
│  Level 5    12dp     FAB expandido, snackbars                      │
│                                                                     │
│  ─── Aplicación ───                                                │
│                                                                     │
│  Card (reposo):        elevation = 1dp → shadow #0000000D          │
│  Card (pressed):       elevation = 3dp → shadow #0000001A          │
│  FAB (reposo):         elevation = 3dp                             │
│  FAB (pressed):        elevation = 6dp                             │
│  BottomSheet:          elevation = 6dp                             │
│  Dialog:               elevation = 8dp                             │
│  Snackbar:             elevation = 6dp                             │
│  TopAppBar (scrolled): elevation = 3dp                             │
│  TopAppBar (top):      elevation = 0dp                             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. Formas y Bordes

### 6.1 Formas (Shape)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    FORMAS (CORNER RADIUS)                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  None         0dp      Bordes rectos                              │
│  Extra Small  4dp      Badges, chips pequeños                     │
│  Small        8dp      Botones, campos de texto, cards pequeñas   │
│  Medium       12dp     Cards, sheets, dialogs                     │
│  Large        16dp     Bottom sheets, modals                      │
│  Extra Large  28dp     FAB, Extended FAB                          │
│  Full         50%      Avatar circular, icon buttons circulares   │
│                                                                     │
│  ─── Ejemplo visual ───                                            │
│                                                                     │
│  ┌────────┐  ┌────────┐  ┌────────┐  ╭────────╮  ┌────────┐     │
│  │ None   │  │ XS 4dp │  │ S 8dp  │  │ M 12dp │  │ L 16dp │     │
│  │        │  │        │  │        │  │        │  ╰────────╯     │
│  └────────┘  └────────┘  └────────┘  ╰────────╯                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Bordes

| Elemento | Ancho | Color | Radio |
|----------|-------|-------|-------|
| OutlinedTextField (enabled) | 1dp | `Outline` | Small (8dp) |
| OutlinedTextField (focused) | 2dp | `Primary` | Small (8dp) |
| OutlinedTextField (error) | 2dp | `Error` | Small (8dp) |
| Card border | 0dp | — | Medium (12dp) |
| Divider | 1dp | `SurfaceVariant` | None |
| Badge | 0dp | — | Extra Small (4dp) |

---

## 7. Iconografía

### 7.1 Iconos por Función

| Función | Icono | Librería |
|---------|-------|----------|
| Navegación atrás | `ArrowBack` | Material Icons |
| Navegación menu | `Menu` | Material Icons |
| Buscar | `Search` | Material Icons |
| Cerrar | `Close` | Material Icons |
| Agregar | `Add` | Material Icons |
| Editar | `Edit` | Material Icons (Outlined) |
| Eliminar | `Delete` | Material Icons (Outlined) |
| Confirmar | `Check` | Material Icons |
| Stock entrada | `ArrowDownward` | Material Icons |
| Stock salida | `ArrowUpward` | Material Icons |
| Producto | `Inventory2` | Material Icons |
| Categoría | `Category` | Material Icons |
| Movimiento | `SwapHoriz` | Material Icons |
| Venta | `PointOfSale` | Material Icons |
| Cliente | `Person` | Material Icons |
| Configuración | `Settings` | Material Icons |
| Notificación | `Notifications` | Material Icons |
| Home | `Home` | Material Icons |
| Organización | `Business` | Material Icons |
| Workspace | `FolderOpen` | Material Icons |
| Alerta stock bajo | `Warning` | Material Icons |

### 7.2 Tamaños de Iconos

| Contexto | Tamaño | Notas |
|----------|--------|-------|
| Icono en IconButton | 24dp | Tamaño estándar |
| Icono en ListItem | 24dp | Leading o trailing |
| Icono en Button | 18dp | After text |
| Icono en Tab | 24dp | Active/inactive |
| Icono en TopAppBar | 24dp | Actions |
| Icono en FAB | 24dp | Centro del FAB |
| Icono grande (empty state) | 64dp | Estados vacíos |
| Icono de status | 16dp | Badges inline |

---

## 8. Animaciones

### 8.1 Duraciones

| Tipo | Duración | Easing |
|------|----------|--------|
| Fast | 150ms | `FastOutSlowIn` |
| Normal | 300ms | `FastOutSlowIn` |
| Slow | 500ms | `FastOutSlowIn` |
| Spring (bouncy) | 400ms | `Spring(dampingRatio = 0.6f)` |
| Spring (stiff) | 300ms | `Spring(stiffness = 300f)` |

### 8.2 Transiciones Comunes

| Elemento | Transición | Notas |
|----------|-----------|-------|
| Screen enter | `fadeIn + slideInHorizontally` | Desde la derecha |
| Screen exit | `fadeOut + slideOutHorizontally` | Hacia la izquierda |
| BottomSheet | `expandVertically + fadeIn` | Desde abajo |
| FAB | `scale` + `shrinkAndExpand` | En su posición |
| List item | `fadeIn + slideInVertically` | Staggered 50ms |
| Loading shimmer | `infiniteTransition` | Placeholder animation |
| Stock badge color | `animateColorAsState` | Smooth transition |

---

## 9. Modo Oscuro / Claro

### 9.1 Implementación

```kotlin
// En Theme.kt
@Composable
fun InventarioAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,  // Material You dynamic color
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

### 9.2 Persistencia

- El usuario puede cambiar el tema manualmente (settings)
- Se guarda en `SettingsDataStore` (ThemeMode: LIGHT / DARK / SYSTEM)
- Se aplica al inicio de la app

---

## 10. Accesibilidad

### 10.1 Requisitos Mínimos

| Requisito | Implementación |
|-----------|---------------|
| Contraste texto | 4.5:1 mínimo (WCAG AA) |
| Contraste gráficos | 3:1 mínimo |
| Tamaño toque mínimo | 48dp × 48dp |
| Content descriptions | Todos los iconos decorativos |
| Screen reader | Labels en todos los elementos interactivos |
| Focus order | Navegación lógica por tabs |
| Text scaling | Soporta hasta 200% sin romper layout |

### 10.2 Content Descriptions

```kotlin
// Ejemplos de contentDescription
Icon(
    imageVector = Icons.Filled.Warning,
    contentDescription = "Stock bajo"
)

IconButton(onClick = { /* delete */ }) {
    Icon(
        imageVector = Icons.Outlined.Delete,
        contentDescription = "Eliminar producto"
    )
}

// En ListItems
ListItem(
    headlineContent = { Text("Widget ABC") },
    supportingContent = { Text("Stock: 45") },
    trailingContent = {
        Icon(Icons.Filled.ChevronRight, contentDescription = "Ver detalle")
    }
)
```

---

## 11. Tokens de Compose

```kotlin
// Color.kt
object AppColors {
    // Primary
    val Primary = Color(0xFF1B5E20)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFA5D6A7)
    val OnPrimaryContainer = Color(0xFF002204)

    // Secondary
    val Secondary = Color(0xFF4A6572)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFC8D8E0)
    val OnSecondaryContainer = Color(0xFF051F2C)

    // Status
    val Success = Color(0xFF2E7D32)
    val SuccessContainer = Color(0xFFC8E6C9)
    val Warning = Color(0xFFF57F17)
    val WarningContainer = Color(0xFFFFF8E1)
    val Error = Color(0xFFB71C1C)
    val ErrorContainer = Color(0xFFFFCDD2)

    // Neutral
    val Background = Color(0xFFFAFAFA)
    val OnBackground = Color(0xFF1C1B1F)
    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF1C1B1F)
    val OnSurfaceVariant = Color(0xFF49454F)
    val Outline = Color(0xFF79747E)
    val OutlineVariant = Color(0xFFCAC4D0)
}

// Type.kt
val AppTypography = Typography(
    displayLarge = TextStyle(fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.15.sp),
    titleSmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
)

// Shapes.kt
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// Spacing.kt
data class Spacing(
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }
```
