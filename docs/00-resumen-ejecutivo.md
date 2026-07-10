# Resumen Ejecutivo — InventarioApp Android

**Proyecto:** Extensión móvil del sistema de inventarios web
**Versión:** 1.0.0
**Fecha:** 2026-07-09

---

## 1. Objetivo

Extender el alcance del sistema de inventarios (Next.js + Neon Postgres) a dispositivos móviles Android, consumiendo la **misma base de datos**, la **misma API REST** y el **mismo sistema de autenticación y autorización** que la aplicación web.

La app móvil no es un producto independiente — es una **extensión nativa** que opera sobre los mismos datos, con las mismas reglas de negocio y el mismo modelo de control de acceso multi-tenant.

---

## 2. Alcance

### Incluido en la app móvil

| Módulo | Funcionalidades |
|--------|-----------------|
| **Autenticación** | Login con Neon Auth (Bearer token), selección de Organización y Workspace |
| **Dashboard** | Métricas del workspace: total productos, unidades, stock bajo, valor inventario, últimos movimientos |
| **Productos** | Listar (paginado, búsqueda), crear, editar, eliminar, ver detalle con historial de precios |
| **Categorías** | Listar, crear, editar |
| **Movimientos** | Registrar entrada/salida con validación de stock, listar con filtros por tipo |
| **Ventas** | Crear venta multi-item, ver detalle, cancelar, listar |
| **Clientes** | Listar (búsqueda), crear, editar |
| **Notificaciones** | Recibir notificaciones push vía Firebase Cloud Messaging |
| **Configuración** | Ver perfil, cambiar workspace, modo claro/oscuro, cerrar sesión |

### No incluido (solo web)

| Funcionalidad | Razón |
|---------------|-------|
| Gestión de Usuarios y Roles | Invitaciones y configuración RBAC se manejan en web |
| Gestión de Organizaciones | Crear/editar org es funcionalidad de admin en web |
| Gestión de Workspaces | Crear/editar ws es funcionalidad de admin en web |
| Onboarding de nuevos usuarios | Flujo de primera vez, solo web |
| Auditoría e Historial de Movimientos | Visibilidad de admin, solo web |
| Órdenes de Compra | Gestión de admin, solo web |
| Cotizaciones | Funcionalidad futura |
| Importación de Productos (CSV/Excel) | Requiere file picker nativo, futura implementación |
| Exportación de Reportes | Requiere generación de archivos, futura implementación |

---

## 3. Stack Tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Lenguaje | Kotlin | 2.1+ |
| UI Framework | Jetpack Compose + Material3 | BOM 2024.12 |
| Arquitectura | Clean Architecture + MVVM | — |
| DI | Hilt (Dagger) | 2.53 |
| Networking | Ktor Client | 3.1.0 |
| DB Local | Room | 2.7.1 |
| Preferences | DataStore (Proto) | 1.1.7 |
| Navigation | Navigation Compose | 2.8.5 |
| Image Loading | Coil 3 | 3.1.0 |
| Testing | JUnit 5 + Mockk + Turbine | — |
| Crash Reporting | Firebase Crashlytics | — |
| Beta Distribution | Firebase App Distribution | — |
| Build CI/CD | GitHub Actions | — |

---

## 4. Fuentes de Verdad

| Elemento | Fuente |
|----------|--------|
| Entidades de datos | `prisma/schema.prisma` |
| API REST | `src/app/api/` (Next.js App Router) |
| Autenticación | Neon Auth (Better Auth) + Bearer plugin |
| RBAC | `src/lib/permisos.ts` + `src/lib/rbac/` |
| Multi-tenancy | `resolverContextoTenant()` + `getTenantPrisma()` |
| Configuración de deployment | `vercel.json` + Vercel dashboard |

---

## 5. Infraestructura

```
┌─────────────────────────────────────────────────────────────┐
│                    INFRAESTRUCTURA                           │
│                                                             │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │ Android App  │───▶│  Vercel      │───▶│  Neon        │  │
│  │ (Kotlin)     │    │  (Next.js)   │    │  (Postgres)  │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│         │                   │                   │           │
│         ▼                   ▼                   ▼           │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  Firebase    │    │  Neon Auth   │    │  Upstash     │  │
│  │  (Crashlytics│    │  (Bearer)    │    │  (Redis)     │  │
│  │   + FCM)     │    │              │    │              │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│                                                             │
│  Costo nuevo: $25 (Google Play Console, pago único)        │
│  Servicios Firebase: gratuitos (tier free)                  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 6. Fases de Desarrollo

| Fase | Contenido | UC | Días |
|------|-----------|-----|------|
| 0 | Setup proyecto + dependencias + estructura | — | 1 |
| 1 | Auth + multi-tenancy + dashboard | UC01-UC04, UC20 | 4 |
| 2 | Productos + categorías + movimientos | UC05-UC13 | 4 |
| 3 | Ventas + clientes | UC14-UC19 | 3 |
| 4 | Notificaciones + perfil + polish | UC21-UC23 | 2 |
| **Total** | | | **14** |

---

## 7. Riesgos Principales

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|------------|
| Bearer plugin no funciona como se espera | Baja | Alto | Testear en staging antes de producción |
| CORS no configurado en Vercel | Media | Alto | Agregar middleware en `src/middleware.ts` |
| API schema cambia (Prisma) | Media | Medio | DTOs como capa de abstracción |
| Room sync conflicts | Baja | Bajo | Server-wins strategy |
| Performance en listas grandes | Media | Medio | Paging 3 + lazy loading |
