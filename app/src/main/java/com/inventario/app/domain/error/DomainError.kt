package com.inventario.app.domain.error

sealed class DomainError(message: String) : Throwable(message) {
    object NetworkError : DomainError("Sin conexion a internet")
    object Unauthorized : DomainError("Sesion expirada")
    object Forbidden : DomainError("No tienes permisos para esta accion")
    data class NotFound(val msg: String) : DomainError(msg)
    data class ServerError(val code: Int) : DomainError("Error del servidor ($code)")
    data class Validation(val field: String, val msg: String) : DomainError(msg)
}
