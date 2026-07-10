# AGENTS.md — InventarioApp Android

> ⚠️ **REGLAS ESTRICTAS**: No hacer push directo a `main`. Ver sección "Branch & Deployment" abajo.

---

## Branch Naming & Deployment Pipeline (STRICT)

### Branch Naming
`[type]/[issue_number]-[brief-description]` — lowercase, singular.
**Prefixes:** `feat/`, `fix/`, `docs/`, `refactor/`, `chore/`, `test/`

### Deployment Pipeline
**NEVER** push to `main` directly. The ONLY path:
1. Push feature branch to origin
2. Open PR from feature branch → `main`
3. CI passes (tests + build) → merge → CD pipeline auto-deploys
4. CD: Build release APK → Firebase App Distribution (beta testers)

**NEVER** run `gradlew assembleRelease` without testing first.

---

## User Preferences

- **Minimal Interaction:** Default to direct execution over conversational gates. When a design or approach is clear, create and execute rather than pausing for approval.
- **Concise Outputs:** Keep messages brief. Avoid unnecessary preambles or explanations.

## AI Document Storage
All AI-generated documents (plans, progress, reports, briefs, specs) live under `docs/` and are excluded from version control:
- Plans → `docs/inner/plans/`
- Progress → `docs/inner/progress/`
- Reports → `docs/inner/reports/`
- Briefs → `docs/inner/briefs/`

---

## Quick Commands

```bash
# Build & install
./gradlew assembleDebug
./gradlew installDebug

# Run tests
./gradlew testDebugUnitTest

# Build release (signed via CI/CD)
./gradlew assembleRelease

# Clean
./gradlew clean

# Run specific test
./gradlew testDebugUnitTest --tests "com.inventario.app.ui.auth.LoginViewModelTest"

# Generate coverage report
./gradlew koverHtmlReportDebug
```

---

## Architecture Highlights

- **Kotlin 2.1+** + **Jetpack Compose** + **Material3**
- **Clean Architecture** (Presentation → Domain → Data)
- **Hilt** for dependency injection
- **Ktor Client** for networking (Neon Auth Bearer token)
- **Room** for offline-first caching
- **DataStore** for preferences (theme, auth token, workspace)
- **Paging 3** for infinite scroll lists
- **Firebase** Analytics + Crashlytics + App Distribution

## Multi-tenant Architecture (Same as Backend)

The app consumes the same hierarchical model from the backend:

```
Organization (Tenant)          ← Administrative scope (organizationId)
  └── Workspace                ← Operative scope (workspaceId)
        └── Operative data     (Producto, Categoria, Movimiento, Venta, Cliente)
  └── OrgMembership            ← RBAC at org level
        └── WSMembership       ← Granularity at workspace level
```

### RBAC en el Android
1. **Global (Usuario.rol):** `SUPER_ADMIN` / `ADMIN` / `USUARIO` — bypass total para admin
2. **Org (OrgMembership.role):** `OWNER` / `ADMIN` / `MEMBER` — acceso a workspaces
3. **WS (WSMembership.role):** `WS-Admin` / `WS-Editor` / `WS-Viewer` — permisos de lectura/escritura
4. **Permisos granulares:** `VER_ANALISIS`, `REGISTRAR_MOVIMIENTOS`, `REALIZAR_VENTAS`, etc.

Todas las validaciones se hacen en el backend. El Android refleja permisos en UI (mostrar/ocultar botones).

---

## UI Component Map

```
ui/
├── auth/                    Login, sesion
├── onboarding/              Org + Workspace selector
├── home/                    Dashboard con metricas
├── producto/                CRUD de productos
├── categoria/               CRUD de categorias
├── movimiento/              Entradas/salidas de stock
├── venta/                   Ventas multi-item
├── cliente/                 CRUD de clientes
├── settings/                Perfil, tema, cerrar sesion
├── notificaciones/          Lista de notificaciones
├── components/              Reutilizables (StockBadge, MetricCard, Shimmer, etc.)
└── navigation/              AppNavigation + Screen routes
```

---

## Key Configuration & Gotchas

### Firebase
- `google-services.json` en `app/` — **NO** versionar (agregado a .gitignore)
- `FIREBASE_CREDENTIALS` como secret de GitHub para CI/CD
- Para Firebase Console: registrar app con package `com.inventario.app`

### Neon Auth (Bearer)
- AuthDataStore guarda token en EncryptedSharedPreferences
- AuthInterceptor lo inyecta en cada request via `Authorization: Bearer <token>`
- Session se obtiene via `GET /api/auth/get-session`

### Build
- `compileSdk = 35`, `minSdk = 26`, `targetSdk = 35`
- R8 activo en release: si agrega librerias nuevas, revisar `proguard-rules.pro`
- Ktor requiere `-dontwarn java.lang.management.**` en release

### Testing
- `./gradlew testDebugUnitTest` para tests unitarios
- Usar `MainDispatcherRule` para ViewModels con corrutinas
- MockK para mocks, Turbine para Flows, Truth para aserciones
- **NO** ejecutar tests en paralelo si usan DB compartida

### Gradle
- Gradle 8.11.1 + JDK 17
- Version catalog en `gradle/libs.versions.toml`
- `ANDROID_HOME` debe apuntar al SDK (configurado en `local.properties`)
