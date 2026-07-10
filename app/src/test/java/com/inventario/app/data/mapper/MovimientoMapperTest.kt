package com.inventario.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.local.db.entity.MovimientoEntity
import com.inventario.app.data.remote.dto.MovimientoDto
import com.inventario.app.domain.model.TipoMovimiento
import org.junit.Test

class MovimientoMapperTest {

    private val sampleDto = MovimientoDto(
        id = 1,
        productoId = 10,
        productoNombre = "Widget A",
        tipo = "entrada",
        cantidad = 50,
        notas = "Compra inicial",
        ventaId = null,
        ordenCompraId = null,
        creadoEn = "2024-01-15T10:30:00",
        usuarioNombre = "Admin"
    )

    private val sampleEntity = MovimientoEntity(
        id = 1,
        productoId = 10,
        productoNombre = "Widget A",
        tipo = "entrada",
        cantidad = 50,
        notas = "Compra inicial",
        ventaId = null,
        ordenCompraId = null,
        workspaceId = "ws-001",
        creadoEn = "2024-01-15T10:30:00",
        usuarioNombre = "Admin"
    )

    // --- MovimientoDto.toDomain ---

    @Test
    fun `MovimientoDto toDomain maps id correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.id).isEqualTo(1)
    }

    @Test
    fun `MovimientoDto toDomain maps tipo entrada correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.entrada)
    }

    @Test
    fun `MovimientoDto toDomain maps tipo salida correctly`() {
        val dto = sampleDto.copy(tipo = "salida")
        val domain = dto.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.salida)
    }

    @Test
    fun `MovimientoDto toDomain maps unknown tipo to entrada`() {
        val dto = sampleDto.copy(tipo = "unknown")
        val domain = dto.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.entrada)
    }

    @Test
    fun `MovimientoDto toDomain maps cantidad correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.cantidad).isEqualTo(50)
    }

    @Test
    fun `MovimientoDto toDomain maps all fields`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.productoId).isEqualTo(10)
        assertThat(domain.productoNombre).isEqualTo("Widget A")
        assertThat(domain.notas).isEqualTo("Compra inicial")
        assertThat(domain.ventaId).isNull()
        assertThat(domain.ordenCompraId).isNull()
        assertThat(domain.creadoEn).isEqualTo("2024-01-15T10:30:00")
        assertThat(domain.usuarioNombre).isEqualTo("Admin")
    }

    @Test
    fun `MovimientoDto toDomain handles null creadoEn as empty string`() {
        val dto = sampleDto.copy(creadoEn = null)
        val domain = dto.toDomain()
        assertThat(domain.creadoEn).isEqualTo("")
    }

    @Test
    fun `MovimientoDto toDomain handles null productoNombre`() {
        val dto = sampleDto.copy(productoNombre = null)
        val domain = dto.toDomain()
        assertThat(domain.productoNombre).isNull()
    }

    @Test
    fun `MovimientoDto toDomain handles null notas`() {
        val dto = sampleDto.copy(notas = null)
        val domain = dto.toDomain()
        assertThat(domain.notas).isNull()
    }

    @Test
    fun `MovimientoDto toDomain handles null usuarioNombre`() {
        val dto = sampleDto.copy(usuarioNombre = null)
        val domain = dto.toDomain()
        assertThat(domain.usuarioNombre).isNull()
    }

    @Test
    fun `MovimientoDto toDomain maps salida with ventaId`() {
        val dto = sampleDto.copy(tipo = "salida", ventaId = 123)
        val domain = dto.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.salida)
        assertThat(domain.ventaId).isEqualTo(123)
    }

    // --- MovimientoDto.toEntity ---

    @Test
    fun `MovimientoDto toEntity maps workspaceId`() {
        val entity = sampleDto.toEntity("ws-001")
        assertThat(entity.workspaceId).isEqualTo("ws-001")
    }

    @Test
    fun `MovimientoDto toEntity maps all fields correctly`() {
        val entity = sampleDto.toEntity("ws-001")
        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.productoId).isEqualTo(10)
        assertThat(entity.productoNombre).isEqualTo("Widget A")
        assertThat(entity.tipo).isEqualTo("entrada")
        assertThat(entity.cantidad).isEqualTo(50)
        assertThat(entity.notas).isEqualTo("Compra inicial")
        assertThat(entity.creadoEn).isEqualTo("2024-01-15T10:30:00")
    }

    @Test
    fun `MovimientoDto toEntity handles null creadoEn as empty string`() {
        val dto = sampleDto.copy(creadoEn = null)
        val entity = dto.toEntity("ws-001")
        assertThat(entity.creadoEn).isEqualTo("")
    }

    @Test
    fun `MovimientoDto toEntity preserves tipo string`() {
        val dto = sampleDto.copy(tipo = "salida")
        val entity = dto.toEntity("ws-001")
        assertThat(entity.tipo).isEqualTo("salida")
    }

    // --- MovimientoEntity.toDomain ---

    @Test
    fun `MovimientoEntity toDomain maps id correctly`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain.id).isEqualTo(1)
    }

    @Test
    fun `MovimientoEntity toDomain maps tipo entrada correctly`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.entrada)
    }

    @Test
    fun `MovimientoEntity toDomain maps tipo salida correctly`() {
        val entity = sampleEntity.copy(tipo = "salida")
        val domain = entity.toDomain()
        assertThat(domain.tipo).isEqualTo(TipoMovimiento.salida)
    }

    @Test
    fun `MovimientoEntity toDomain maps cantidad correctly`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain.cantidad).isEqualTo(50)
    }

    @Test
    fun `MovimientoEntity toDomain maps all fields`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain.productoId).isEqualTo(10)
        assertThat(domain.productoNombre).isEqualTo("Widget A")
        assertThat(domain.notas).isEqualTo("Compra inicial")
        assertThat(domain.creadoEn).isEqualTo("2024-01-15T10:30:00")
        assertThat(domain.usuarioNombre).isEqualTo("Admin")
    }

    @Test
    fun `MovimientoEntity toDomain handles null optional fields`() {
        val entity = sampleEntity.copy(productoNombre = null, notas = null, usuarioNombre = null)
        val domain = entity.toDomain()
        assertThat(domain.productoNombre).isNull()
        assertThat(domain.notas).isNull()
        assertThat(domain.usuarioNombre).isNull()
    }

    @Test
    fun `MovimientoEntity toDomain ignores workspaceId`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain).isNotInstanceOf(MovimientoEntity::class.java)
    }

    // --- Round-trip: Dto -> Domain -> Entity -> Domain ---

    @Test
    fun `Dto to Domain to Entity roundtrip preserves data`() {
        val domain = sampleDto.toDomain()
        val entity = MovimientoEntity(
            id = domain.id,
            productoId = domain.productoId,
            productoNombre = domain.productoNombre,
            tipo = if (domain.tipo == TipoMovimiento.entrada) "entrada" else "salida",
            cantidad = domain.cantidad,
            notas = domain.notas,
            ventaId = domain.ventaId,
            ordenCompraId = domain.ordenCompraId,
            workspaceId = "ws-001",
            creadoEn = domain.creadoEn,
            usuarioNombre = domain.usuarioNombre
        )
        val roundtripped = entity.toDomain()
        assertThat(roundtripped.id).isEqualTo(domain.id)
        assertThat(roundtripped.cantidad).isEqualTo(domain.cantidad)
        assertThat(roundtripped.tipo).isEqualTo(domain.tipo)
    }
}
