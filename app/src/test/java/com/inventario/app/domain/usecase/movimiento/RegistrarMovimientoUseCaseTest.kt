package com.inventario.app.domain.usecase.movimiento

import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.repository.MovimientoRepository
import com.inventario.app.domain.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegistrarMovimientoUseCaseTest {

    private val repository = mockk<MovimientoRepository>()
    private val productoRepository = mockk<ProductoRepository>()
    private lateinit var useCase: RegistrarMovimientoUseCase

    private val sampleMovimiento = Movimiento(
        id = 1,
        productoId = 1,
        productoNombre = "Widget A",
        tipo = TipoMovimiento.entrada,
        cantidad = 50,
        notas = null,
        ventaId = null,
        ordenCompraId = null,
        creadoEn = "2024-01-15T10:30:00",
        usuarioNombre = "Test User"
    )

    private val sampleProducto = Producto(
        id = 1,
        nombre = "Widget A",
        descripcion = null,
        codigo = "WA001",
        precio = 15000.0,
        cantidad = 100,
        stockMinimo = 10,
        categoriaId = 1,
        categoriaNombre = "Categoría 1",
        creadoEn = "2024-01-01"
    )

    @Before
    fun setup() {
        useCase = RegistrarMovimientoUseCase(repository, productoRepository)
    }

    @Test
    fun `invoke returns failure for zero quantity`() = runTest {
        val result = useCase(1, TipoMovimiento.entrada, 0, null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("La cantidad debe ser mayor a 0")
    }

    @Test
    fun `invoke returns failure for negative quantity`() = runTest {
        val result = useCase(1, TipoMovimiento.entrada, -5, null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("La cantidad debe ser mayor a 0")
    }

    @Test
    fun `invoke allows entrada without stock check`() = runTest {
        coEvery { repository.registrarMovimiento(1, TipoMovimiento.entrada, 50, null) } returns Result.success(
            sampleMovimiento.copy(cantidad = 50, tipo = TipoMovimiento.entrada)
        )

        val result = useCase(1, TipoMovimiento.entrada, 50, null)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke checks stock for salida`() = runTest {
        coEvery { productoRepository.getProductoById(1) } returns Result.success(sampleProducto)
        coEvery { repository.registrarMovimiento(1, TipoMovimiento.salida, 5, null) } returns Result.success(
            sampleMovimiento.copy(cantidad = 5, tipo = TipoMovimiento.salida)
        )

        val result = useCase(1, TipoMovimiento.salida, 5, null)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke fails when stock insufficient for salida`() = runTest {
        val lowStockProducto = sampleProducto.copy(cantidad = 3)
        coEvery { productoRepository.getProductoById(1) } returns Result.success(lowStockProducto)

        val result = useCase(1, TipoMovimiento.salida, 5, null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Stock insuficiente")
    }

    @Test
    fun `invoke fails when producto not found for salida`() = runTest {
        coEvery { productoRepository.getProductoById(999) } returns Result.failure(Exception("Not found"))

        val result = useCase(999, TipoMovimiento.salida, 5, null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Producto no encontrado")
    }

    @Test
    fun `invoke passes notas to repository`() = runTest {
        val notas = "Compra proveedor XYZ"
        coEvery { repository.registrarMovimiento(1, TipoMovimiento.entrada, 50, notas) } returns Result.success(
            sampleMovimiento.copy(notas = notas)
        )

        val result = useCase(1, TipoMovimiento.entrada, 50, notas)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke handles null notas`() = runTest {
        coEvery { repository.registrarMovimiento(1, TipoMovimiento.entrada, 50, null) } returns Result.success(
            sampleMovimiento
        )

        val result = useCase(1, TipoMovimiento.entrada, 50, null)

        assertThat(result.isSuccess).isTrue()
    }
}
