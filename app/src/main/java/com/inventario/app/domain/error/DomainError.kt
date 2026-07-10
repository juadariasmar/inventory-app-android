package com.inventario.app.domain.error

sealed class DomainError(message: String) : Throwable(message) {
    object NetworkError : DomainError("No se pudo conectar con el servidor. Verifica tu conexion a internet.")
    object Timeout : DomainError("El servidor no respondio a tiempo. Intenta de nuevo.")
    object Unauthorized : DomainError("Sesion expirada. Inicia sesion de nuevo.")
    object Forbidden : DomainError("No tienes permisos para realizar esta accion.")
    data class NotFound(val msg: String) : DomainError(msg)
    data class ServerError(val code: Int) : DomainError("Error del servidor ($code). Intenta mas tarde.")
    data class Validation(val field: String, val msg: String) : DomainError(msg)
}
