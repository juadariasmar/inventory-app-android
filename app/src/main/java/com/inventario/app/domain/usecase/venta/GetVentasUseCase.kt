package com.inventario.app.domain.usecase.venta

import com.inventario.app.domain.model.Venta
import com.inventario.app.domain.repository.VentaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVentasUseCase @Inject constructor(
    private val repository: VentaRepository
) {
    operator fun invoke(): Flow<List<Venta>> = repository.getVentas()
}
