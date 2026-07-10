package com.inventario.app.data.mapper

import com.inventario.app.data.local.db.entity.MovimientoEntity
import com.inventario.app.data.remote.dto.MovimientoDto
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento

fun MovimientoDto.toDomain(): Movimiento {
    return Movimiento(
        id = id,
        productoId = productoId,
        productoNombre = productoNombre,
        tipo = when (tipo) {
            "entrada" -> TipoMovimiento.entrada
            "salida" -> TipoMovimiento.salida
            else -> TipoMovimiento.entrada
        },
        cantidad = cantidad,
        notas = notas,
        ventaId = ventaId,
        ordenCompraId = ordenCompraId,
        creadoEn = creadoEn ?: "",
        usuarioNombre = usuarioNombre
    )
}

fun MovimientoDto.toEntity(workspaceId: String): MovimientoEntity {
    return MovimientoEntity(
        id = id,
        productoId = productoId,
        productoNombre = productoNombre,
        tipo = tipo,
        cantidad = cantidad,
        notas = notas,
        ventaId = ventaId,
        ordenCompraId = ordenCompraId,
        workspaceId = workspaceId,
        creadoEn = creadoEn ?: "",
        usuarioNombre = usuarioNombre
    )
}

fun MovimientoEntity.toDomain(): Movimiento {
    return Movimiento(
        id = id,
        productoId = productoId,
        productoNombre = productoNombre,
        tipo = when (tipo) {
            "entrada" -> TipoMovimiento.entrada
            "salida" -> TipoMovimiento.salida
            else -> TipoMovimiento.entrada
        },
        cantidad = cantidad,
        notas = notas,
        ventaId = ventaId,
        ordenCompraId = ordenCompraId,
        creadoEn = creadoEn,
        usuarioNombre = usuarioNombre
    )
}
