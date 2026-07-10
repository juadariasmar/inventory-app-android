package com.inventario.app.domain.usecase.auth

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(DomainError.Validation("email", "Email es requerido"))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(DomainError.Validation("email", "Email invalido"))
        }
        return authRepository.forgotPassword(email)
    }
}
