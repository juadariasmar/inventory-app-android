package com.inventario.app.domain.usecase.auth

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val workspaceDataStore: WorkspaceDataStore
) {
    suspend operator fun invoke() {
        authRepository.logout()
        workspaceDataStore.clear()
    }
}
