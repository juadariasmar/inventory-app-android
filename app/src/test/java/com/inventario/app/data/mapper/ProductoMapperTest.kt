package com.inventario.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.local.db.entity.ProductoEntity
import com.inventario.app.data.remote.dto.ProductoDto
import com.inventario.app.domain.model.Producto
import org.junit.Test

class ProductoMapperTest {

    private val sampleDto = ProductoDto(
        id = 1,
        nombre = "Widget",
        descripcion = "A test widget",
        codigo = "W001",
        precio = 25000.0,
        cantidad = 45,
        stockMinimo = 10,
        categoriaId = 1,
        categoriaNombre = "Hardware",
        creadoEn = "2024-01-15"
    )

    private val sampleEntity = ProductoEntity(
        id = 1,
        nombre = "Widget",
        descripcion = "A test widget",
        codigo = "W001",
        precio = 25000.0,
        cantidad = 45,
        stockMinimo = 10,
        categoriaId = 1,
        categoriaNombre = "Hardware",
        workspaceId = "ws-001",
        creadoEn = "2024-01-15"
    )

    private val sampleDomain = Producto(
        id = 1,
        nombre = "Widget",
        descripcion = "A test widget",
        codigo = "W001",
        precio = 25000.0,
        cantidad = 45,
        stockMinimo = 10,
        categoriaId = 1,
        categoriaNombre = "Hardware",
        creadoEn = "2024-01-15"
    )

    // --- ProductoDto.toDomain ---

    @Test
    fun `ProductoDto toDomain maps id correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.id).isEqualTo(1)
    }

    @Test
    fun `ProductoDto toDomain maps nombre correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain.nombre).isEqualTo("Widget")
    }

    @Test
    fun `ProductoDto toDomain maps all fields correctly`() {
        val domain = sampleDto.toDomain()
        assertThat(domain).isEqualTo(sampleDomain)
    }

    @Test
    fun `ProductoDto toDomain handles null descripcion`() {
        val dto = sampleDto.copy(descripcion = null)
        val domain = dto.toDomain()
        assertThat(domain.descripcion).isNull()
    }

    @Test
    fun `ProductoDto toDomain handles null creadoEn as empty string`() {
        val dto = sampleDto.copy(creadoEn = null)
        val domain = dto.toDomain()
        assertThat(domain.creadoEn).isEqualTo("")
    }

    @Test
    fun `ProductoDto toDomain handles null categoriaNombre`() {
        val dto = sampleDto.copy(categoriaNombre = null)
        val domain = dto.toDomain()
        assertThat(domain.categoriaNombre).isNull()
    }

    @Test
    fun `ProductoDto toDomain maps precio correctly`() {
        val dto = sampleDto.copy(precio = 99999.50)
        val domain = dto.toDomain()
        assertThat(domain.precio).isWithin(0.01).of(99999.50)
    }

    @Test
    fun `ProductoDto toDomain maps stockMinimo correctly`() {
        val dto = sampleDto.copy(stockMinimo = 5)
        val domain = dto.toDomain()
        assertThat(domain.stockMinimo).isEqualTo(5)
    }

    // --- ProductoDto.toEntity ---

    @Test
    fun `ProductoDto toEntity maps workspaceId`() {
        val entity = sampleDto.toEntity("ws-001")
        assertThat(entity.workspaceId).isEqualTo("ws-001")
    }

    @Test
    fun `ProductoDto toEntity maps all fields correctly`() {
        val entity = sampleDto.toEntity("ws-001")
        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.nombre).isEqualTo("Widget")
        assertThat(entity.codigo).isEqualTo("W001")
        assertThat(entity.precio).isWithin(0.01).of(25000.0)
        assertThat(entity.cantidad).isEqualTo(45)
        assertThat(entity.stockMinimo).isEqualTo(10)
        assertThat(entity.categoriaId).isEqualTo(1)
        assertThat(entity.categoriaNombre).isEqualTo("Hardware")
        assertThat(entity.creadoEn).isEqualTo("2024-01-15")
    }

    @Test
    fun `ProductoDto toEntity handles null creadoEn as empty string`() {
        val dto = sampleDto.copy(creadoEn = null)
        val entity = dto.toEntity("ws-001")
        assertThat(entity.creadoEn).isEqualTo("")
    }

    @Test
    fun `ProductoDto toEntity handles null descripcion`() {
        val dto = sampleDto.copy(descripcion = null)
        val entity = dto.toEntity("ws-001")
        assertThat(entity.descripcion).isNull()
    }

    // --- ProductoEntity.toDomain ---

    @Test
    fun `ProductoEntity toDomain maps all fields correctly`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain.id).isEqualTo(1)
        assertThat(domain.nombre).isEqualTo("Widget")
        assertThat(domain.descripcion).isEqualTo("A test widget")
        assertThat(domain.codigo).isEqualTo("W001")
        assertThat(domain.precio).isWithin(0.01).of(25000.0)
        assertThat(domain.cantidad).isEqualTo(45)
        assertThat(domain.stockMinimo).isEqualTo(10)
        assertThat(domain.categoriaId).isEqualTo(1)
        assertThat(domain.categoriaNombre).isEqualTo("Hardware")
        assertThat(domain.creadoEn).isEqualTo("2024-01-15")
    }

    @Test
    fun `ProductoEntity toDomain handles null optional fields`() {
        val entity = sampleEntity.copy(descripcion = null, categoriaNombre = null)
        val domain = entity.toDomain()
        assertThat(domain.descripcion).isNull()
        assertThat(domain.categoriaNombre).isNull()
    }

    @Test
    fun `ProductoEntity toDomain ignores workspaceId`() {
        val domain = sampleEntity.toDomain()
        assertThat(domain).isEqualTo(sampleDomain)
    }

    // --- Producto.toDto ---

    @Test
    fun `Producto toDto maps all fields correctly`() {
        val dto = sampleDomain.toDto()
        assertThat(dto.id).isEqualTo(1)
        assertThat(dto.nombre).isEqualTo("Widget")
        assertThat(dto.descripcion).isEqualTo("A test widget")
        assertThat(dto.codigo).isEqualTo("W001")
        assertThat(dto.precio).isWithin(0.01).of(25000.0)
        assertThat(dto.cantidad).isEqualTo(45)
        assertThat(dto.stockMinimo).isEqualTo(10)
        assertThat(dto.categoriaId).isEqualTo(1)
        assertThat(dto.categoriaNombre).isEqualTo("Hardware")
        assertThat(dto.creadoEn).isEqualTo("2024-01-15")
    }

    @Test
    fun `Producto toDto handles null optional fields`() {
        val domain = sampleDomain.copy(descripcion = null, categoriaNombre = null)
        val dto = domain.toDto()
        assertThat(dto.descripcion).isNull()
        assertThat(dto.categoriaNombre).isNull()
    }

    // --- Round-trip mapping ---

    @Test
    fun `Dto to Domain to Dto roundtrip preserves data`() {
        val roundtripped = sampleDto.toDomain().toDto()
        assertThat(roundtripped.id).isEqualTo(sampleDto.id)
        assertThat(roundtripped.nombre).isEqualTo(sampleDto.nombre)
        assertThat(roundtripped.codigo).isEqualTo(sampleDto.codigo)
        assertThat(roundtripped.precio).isWithin(0.01).of(sampleDto.precio)
        assertThat(roundtripped.cantidad).isEqualTo(sampleDto.cantidad)
    }

    @Test
    fun `Entity to Domain to Entity roundtrip preserves data`() {
        val roundtripped = sampleEntity.toDomain().toEntity(sampleEntity.workspaceId)
        assertThat(roundtripped.id).isEqualTo(sampleEntity.id)
        assertThat(roundtripped.nombre).isEqualTo(sampleEntity.nombre)
        assertThat(roundtripped.codigo).isEqualTo(sampleEntity.codigo)
        assertThat(roundtripped.workspaceId).isEqualTo(sampleEntity.workspaceId)
    }
}
