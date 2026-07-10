package com.inventario.app.domain.usecase.auth

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Sesion> {
        if (email.isBlank()) return Result.failure(DomainError.Validation("email", "Email es requerido"))
        if (password.isBlank()) return Result.failure(DomainError.Validation("password", "Contraseña es requerida"))
        return authRepository.login(email, password)
    }
}
