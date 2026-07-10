package com.inventario.app.domain.error

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DomainErrorTest {

    @Test
    fun `NetworkError has correct message`() {
        val error = DomainError.NetworkError
        assertThat(error.message).isEqualTo("Sin conexion a internet")
    }

    @Test
    fun `Unauthorized has correct message`() {
        val error = DomainError.Unauthorized
        assertThat(error.message).isEqualTo("Sesion expirada")
    }

    @Test
    fun `Forbidden has correct message`() {
        val error = DomainError.Forbidden
        assertThat(error.message).isEqualTo("No tienes permisos para esta accion")
    }

    @Test
    fun `NotFound carries custom message`() {
        val error = DomainError.NotFound("Producto no encontrado")
        assertThat(error.message).isEqualTo("Producto no encontrado")
        assertThat(error.msg).isEqualTo("Producto no encontrado")
    }

    @Test
    fun `ServerError carries status code in message`() {
        val error = DomainError.ServerError(500)
        assertThat(error.message).isEqualTo("Error del servidor (500)")
        assertThat(error.code).isEqualTo(500)
    }

    @Test
    fun `ServerError with different code`() {
        val error = DomainError.ServerError(503)
        assertThat(error.code).isEqualTo(503)
    }

    @Test
    fun `Validation carries field name and message`() {
        val error = DomainError.Validation("email", "Email es requerido")
        assertThat(error.field).isEqualTo("email")
        assertThat(error.msg).isEqualTo("Email es requerido")
        assertThat(error.message).isEqualTo("Email es requerido")
    }

    @Test
    fun `Validation with different field`() {
        val error = DomainError.Validation("password", "Contraseña es requerida")
        assertThat(error.field).isEqualTo("password")
    }

    @Test
    fun `all errors are DomainError subtypes`() {
        val errors: List<DomainError> = listOf(
            DomainError.NetworkError,
            DomainError.Unauthorized,
            DomainError.Forbidden,
            DomainError.NotFound("test"),
            DomainError.ServerError(500),
            Validation("field", "msg")
        )
        errors.forEach { assertThat(it).isInstanceOf(DomainError::class.java) }
    }
}
