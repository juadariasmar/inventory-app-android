package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.ItemVentaDto
import com.inventario.app.data.remote.dto.VentaDto
import com.inventario.app.domain.model.EstadoVenta
import com.inventario.app.domain.model.ItemVenta
import com.inventario.app.domain.model.Venta

fun VentaDto.toDomain(): Venta {
    return Venta(
        id = id,
        vendedorNombre = vendedorNombre,
        clienteNombre = clienteNombre,
        total = total,
        notas = notas,
        items = items.map { it.toDomain() },
        canceladaEn = canceladaEn,
        motivoCancelacion = motivoCancelacion,
        creadoEn = creadoEn ?: ""
    )
}

fun ItemVentaDto.toDomain(): ItemVenta {
    return ItemVenta(
        id = id,
        productoId = productoId,
        productoNombre = productoNombre,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}
