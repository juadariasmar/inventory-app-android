package com.inventario.app.domain.usecase.movimiento

import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.repository.MovimientoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovimientosUseCase @Inject constructor(
    private val repository: MovimientoRepository
) {
    operator fun invoke(filtroTipo: TipoMovimiento? = null): Flow<List<Movimiento>> =
        repository.getMovimientos(filtroTipo)
}
