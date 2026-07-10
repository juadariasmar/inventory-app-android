package com.inventario.app.domain.usecase.auth

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class LogoutUseCaseTest {

    private val authRepository = mockk<AuthRepository>(relaxed = true)
    private val workspaceDataStore = mockk<WorkspaceDataStore>(relaxed = true)
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setup() {
        useCase = LogoutUseCase(authRepository, workspaceDataStore)
    }

    @Test
    fun `invoke calls authRepository logout`() = runTest {
        useCase()

        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun `invoke calls workspaceDataStore clear`() = runTest {
        useCase()

        coVerify(exactly = 1) { workspaceDataStore.clear() }
    }

    @Test
    fun `invoke clears both auth and workspace data in order`() = runTest {
        useCase()

        coVerify(ordering = io.mockk.Ordering.ORDERED) {
            authRepository.logout()
            workspaceDataStore.clear()
        }
    }

    @Test
    fun `invoke propagates exception when authRepository throws`() = runTest {
        coEvery { authRepository.logout() } throws RuntimeException("network error")

        assertFailsWith<RuntimeException> {
            useCase()
        }

        coVerify { authRepository.logout() }
        coVerify(exactly = 0) { workspaceDataStore.clear() }
    }

    @Test
    fun `invoke propagates exception when workspaceDataStore throws`() = runTest {
        coEvery { workspaceDataStore.clear() } throws RuntimeException("storage error")

        assertFailsWith<RuntimeException> {
            useCase()
        }

        coVerify { authRepository.logout() }
        coVerify { workspaceDataStore.clear() }
    }
}
