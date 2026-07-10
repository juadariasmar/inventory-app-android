package com.inventario.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.remote.dto.ItemVentaDto
import com.inventario.app.data.remote.dto.VentaDto
import com.inventario.app.domain.model.ItemVenta
import org.junit.Test

class VentaMapperTest {

    private val sampleItemDto = ItemVentaDto(
        id = 1,
        productoId = 10,
        productoNombre = "Widget",
        cantidad = 2,
        precioUnitario = 100.0,
        subtotal = 200.0
    )

    private val sampleVentaDto = VentaDto(
        id = 1,
        vendedorNombre = "Vendedor Test",
        clienteNombre = "Cliente Test",
        total = 350.0,
        notas = "Nota de venta",
        items = listOf(
            sampleItemDto,
            ItemVentaDto(2, 20, "Gadget", 3, 50.0, 150.0)
        ),
        canceladaEn = null,
        motivoCancelacion = null,
        creadoEn = "2024-01-15T10:30:00Z"
    )

    // --- VentaDto.toDomain ---

    @Test
    fun `VentaDto toDomain maps id correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.id).isEqualTo(1)
    }

    @Test
    fun `VentaDto toDomain maps vendedorNombre correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.vendedorNombre).isEqualTo("Vendedor Test")
    }

    @Test
    fun `VentaDto toDomain maps clienteNombre correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.clienteNombre).isEqualTo("Cliente Test")
    }

    @Test
    fun `VentaDto toDomain maps total correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.total).isWithin(0.01).of(350.0)
    }

    @Test
    fun `VentaDto toDomain maps items correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.items).hasSize(2)
        assertThat(domain.items[0].productoNombre).isEqualTo("Widget")
        assertThat(domain.items[1].productoNombre).isEqualTo("Gadget")
    }

    @Test
    fun `VentaDto toDomain maps notas correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.notas).isEqualTo("Nota de venta")
    }

    @Test
    fun `VentaDto toDomain maps creadoEn correctly`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.creadoEn).isEqualTo("2024-01-15T10:30:00Z")
    }

    @Test
    fun `VentaDto toDomain maps null fields`() {
        val dto = VentaDto(
            id = 2,
            total = 0.0,
            items = emptyList(),
            creadoEn = null
        )
        val domain = dto.toDomain()
        assertThat(domain.vendedorNombre).isNull()
        assertThat(domain.clienteNombre).isNull()
        assertThat(domain.notas).isNull()
        assertThat(domain.canceladaEn).isNull()
        assertThat(domain.motivoCancelacion).isNull()
        assertThat(domain.creadoEn).isEqualTo("")
    }

    @Test
    fun `VentaDto toDomain handles canceladaEn`() {
        val dto = sampleVentaDto.copy(canceladaEn = "2024-01-16T10:00:00Z", motivoCancelacion = "Cliente canceló")
        val domain = dto.toDomain()
        assertThat(domain.canceladaEn).isEqualTo("2024-01-16T10:00:00Z")
        assertThat(domain.motivoCancelacion).isEqualTo("Cliente canceló")
    }

    @Test
    fun `VentaDto toDomain handles empty items`() {
        val dto = sampleVentaDto.copy(items = emptyList())
        val domain = dto.toDomain()
        assertThat(domain.items).isEmpty()
    }

    @Test
    fun `VentaDto toDomain maps item subtotal`() {
        val domain = sampleVentaDto.toDomain()
        assertThat(domain.items[0].subtotal).isWithin(0.01).of(200.0)
    }

    // --- ItemVentaDto.toDomain ---

    @Test
    fun `ItemVentaDto toDomain maps all fields`() {
        val domain = sampleItemDto.toDomain()
        assertThat(domain.id).isEqualTo(1)
        assertThat(domain.productoId).isEqualTo(10)
        assertThat(domain.productoNombre).isEqualTo("Widget")
        assertThat(domain.cantidad).isEqualTo(2)
        assertThat(domain.precioUnitario).isWithin(0.01).of(100.0)
        assertThat(domain.subtotal).isWithin(0.01).of(200.0)
    }

    @Test
    fun `ItemVentaDto toDomain handles null productoNombre`() {
        val dto = sampleItemDto.copy(productoNombre = null)
        val domain = dto.toDomain()
        assertThat(domain.productoNombre).isNull()
    }

    @Test
    fun `ItemVentaDto toDomain handles zero subtotal`() {
        val dto = sampleItemDto.copy(precioUnitario = 0.0, subtotal = 0.0)
        val domain = dto.toDomain()
        assertThat(domain.precioUnitario).isWithin(0.01).of(0.0)
        assertThat(domain.subtotal).isWithin(0.01).of(0.0)
    }
}
