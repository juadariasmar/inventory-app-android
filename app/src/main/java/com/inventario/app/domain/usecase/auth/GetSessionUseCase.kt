package com.inventario.app.domain.usecase.auth

import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.repository.AuthRepository
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Sesion> = authRepository.getSession()
}
