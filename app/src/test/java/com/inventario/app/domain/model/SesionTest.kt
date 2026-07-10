package com.inventario.app.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SesionTest {

    private fun buildSesion(
        rol: String = "USUARIO",
        permisos: List<String> = emptyList()
    ) = Sesion(
        userId = "1",
        email = "test@email.com",
        nombre = "Test User",
        rol = rol,
        estado = "ACTIVO",
        permisos = permisos
    )

    @Test
    fun `esAdmin returns true for SUPER_ADMIN role`() {
        assertTrue(buildSesion(rol = "SUPER_ADMIN").esAdmin())
    }

    @Test
    fun `esAdmin returns true for ADMIN role`() {
        assertTrue(buildSesion(rol = "ADMIN").esAdmin())
    }

    @Test
    fun `esAdmin returns false for USUARIO role`() {
        assertFalse(buildSesion(rol = "USUARIO").esAdmin())
    }

    @Test
    fun `esAdmin returns false for unknown role`() {
        assertFalse(buildSesion(rol = "GUEST").esAdmin())
    }

    @Test
    fun `puedeRegistrarMovimientos returns true for ADMIN`() {
        assertTrue(buildSesion(rol = "ADMIN").puedeRegistrarMovimientos())
    }

    @Test
    fun `puedeRegistrarMovimientos returns true for SUPER_ADMIN`() {
        assertTrue(buildSesion(rol = "SUPER_ADMIN").puedeRegistrarMovimientos())
    }

    @Test
    fun `puedeRegistrarMovimientos returns true for USUARIO with permiso`() {
        assertTrue(
            buildSesion(
                rol = "USUARIO",
                permisos = listOf("REGISTRAR_MOVIMIENTOS")
            ).puedeRegistrarMovimientos()
        )
    }

    @Test
    fun `puedeRegistrarMovimientos returns false for USUARIO without permiso`() {
        assertFalse(
            buildSesion(rol = "USUARIO", permisos = emptyList()).puedeRegistrarMovimientos()
        )
    }

    @Test
    fun `puedeRegistrarMovimientos returns false for USUARIO with unrelated permiso`() {
        assertFalse(
            buildSesion(
                rol = "USUARIO",
                permisos = listOf("REALIZAR_VENTAS")
            ).puedeRegistrarMovimientos()
        )
    }

    @Test
    fun `puedeRealizarVentas returns true for ADMIN`() {
        assertTrue(buildSesion(rol = "ADMIN").puedeRealizarVentas())
    }

    @Test
    fun `puedeRealizarVentas returns true for USUARIO with REALIZAR_VENTAS permiso`() {
        assertTrue(
            buildSesion(
                rol = "USUARIO",
                permisos = listOf("REALIZAR_VENTAS")
            ).puedeRealizarVentas()
        )
    }

    @Test
    fun `puedeRealizarVentas returns false for USUARIO without permiso`() {
        assertFalse(
            buildSesion(rol = "USUARIO", permisos = emptyList()).puedeRealizarVentas()
        )
    }

    @Test
    fun `puedeVerAnalisis returns true for SUPER_ADMIN`() {
        assertTrue(buildSesion(rol = "SUPER_ADMIN").puedeVerAnalisis())
    }

    @Test
    fun `puedeVerAnalisis returns true for USUARIO with VER_ANALISIS permiso`() {
        assertTrue(
            buildSesion(
                rol = "USUARIO",
                permisos = listOf("VER_ANALISIS")
            ).puedeVerAnalisis()
        )
    }

    @Test
    fun `puedeVerAnalisis returns false for USUARIO without permiso`() {
        assertFalse(
            buildSesion(rol = "USUARIO", permisos = emptyList()).puedeVerAnalisis()
        )
    }

    @Test
    fun `multiple permissions are correctly evaluated`() {
        val sesion = buildSesion(
            rol = "USUARIO",
            permisos = listOf("REGISTRAR_MOVIMIENTOS", "REALIZAR_VENTAS")
        )
        assertTrue(sesion.puedeRegistrarMovimientos())
        assertTrue(sesion.puedeRealizarVentas())
        assertFalse(sesion.puedeVerAnalisis())
    }

    @Test
    fun `data class equality works correctly`() {
        val a = Sesion("1", "a@b.com", "Test", "ADMIN", "ACTIVO", emptyList())
        val b = Sesion("1", "a@b.com", "Test", "ADMIN", "ACTIVO", emptyList())
        assertTrue(a == b)
    }

    @Test
    fun `data class inequality works correctly`() {
        val a = Sesion("1", "a@b.com", "Test", "ADMIN", "ACTIVO", emptyList())
        val b = Sesion("2", "a@b.com", "Test", "ADMIN", "ACTIVO", emptyList())
        assertFalse(a == b)
    }
}
