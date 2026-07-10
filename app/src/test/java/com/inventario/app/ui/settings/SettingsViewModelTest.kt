package com.inventario.app.ui.settings

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.SettingsDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.domain.usecase.auth.LogoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settingsDataStore = mockk<SettingsDataStore>()
    private val workspaceDataStore = mockk<WorkspaceDataStore>()
    private val authDataStore = mockk<AuthDataStore>()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        coEvery { settingsDataStore.isDarkMode } returns flowOf(null)
        coEvery { settingsDataStore.useDynamicColor } returns flowOf(false)
        coEvery { workspaceDataStore.wsNombre } returns flowOf("")
        coEvery { workspaceDataStore.orgNombre } returns flowOf("")
        coEvery { authDataStore.userName } returns flowOf("")
        coEvery { authDataStore.userEmail } returns flowOf("")
        coEvery { authDataStore.userRol } returns flowOf("")
        viewModel = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)
    }

    @Test
    fun `initial state loads user data from DataStores`() = runTest {
        coEvery { authDataStore.userName } returns flowOf("Juan Pérez")
        coEvery { authDataStore.userEmail } returns flowOf("juan@test.com")
        coEvery { authDataStore.userRol } returns flowOf("ADMIN")
        coEvery { workspaceDataStore.wsNombre } returns flowOf("Mi Tienda")
        coEvery { workspaceDataStore.orgNombre } returns flowOf("Mi Org")
        val vm = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)

        assertThat(vm.uiState.value.userName).isEqualTo("Juan Pérez")
        assertThat(vm.uiState.value.userEmail).isEqualTo("juan@test.com")
        assertThat(vm.uiState.value.userRol).isEqualTo("ADMIN")
        assertThat(vm.uiState.value.currentWs).isEqualTo("Mi Tienda")
        assertThat(vm.uiState.value.currentOrg).isEqualTo("Mi Org")
    }

    @Test
    fun `initial state loads dark mode preference`() = runTest {
        coEvery { settingsDataStore.isDarkMode } returns flowOf(true)
        val vm = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)

        assertThat(vm.uiState.value.darkModeEnabled).isTrue()
    }

    @Test
    fun `initial state loads dynamic color preference`() = runTest {
        coEvery { settingsDataStore.useDynamicColor } returns flowOf(true)
        val vm = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)

        assertThat(vm.uiState.value.useDynamicColor).isTrue()
    }

    @Test
    fun `onDarkModeChanged updates DataStore with true`() = runTest {
        coEvery { settingsDataStore.setDarkMode(true) } just runs

        viewModel.onDarkModeChanged(true)

        coVerify { settingsDataStore.setDarkMode(true) }
    }

    @Test
    fun `onDarkModeChanged updates DataStore with false`() = runTest {
        coEvery { settingsDataStore.setDarkMode(false) } just runs

        viewModel.onDarkModeChanged(false)

        coVerify { settingsDataStore.setDarkMode(false) }
    }

    @Test
    fun `onDarkModeChanged updates DataStore with null`() = runTest {
        coEvery { settingsDataStore.setDarkMode(null) } just runs

        viewModel.onDarkModeChanged(null)

        coVerify { settingsDataStore.setDarkMode(null) }
    }

    @Test
    fun `onDynamicColorChanged updates DataStore`() = runTest {
        coEvery { settingsDataStore.setUseDynamicColor(true) } just runs

        viewModel.onDynamicColorChanged(true)

        coVerify { settingsDataStore.setUseDynamicColor(true) }
    }

    @Test
    fun `onDynamicColorChanged updates DataStore with false`() = runTest {
        coEvery { settingsDataStore.setUseDynamicColor(false) } just runs

        viewModel.onDynamicColorChanged(false)

        coVerify { settingsDataStore.setUseDynamicColor(false) }
    }

    @Test
    fun `onLogout clears session and emits event`() = runTest {
        coEvery { logoutUseCase() } just runs

        viewModel.onLogout()

        viewModel.logoutEvent.test {
            assertThat(awaitItem()).isTrue()
        }
        coVerify { logoutUseCase() }
    }

    @Test
    fun `handles null values from DataStores as empty strings`() = runTest {
        coEvery { authDataStore.userName } returns flowOf(null)
        coEvery { authDataStore.userEmail } returns flowOf(null)
        coEvery { authDataStore.userRol } returns flowOf(null)
        val vm = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)

        assertThat(vm.uiState.value.userName).isEmpty()
        assertThat(vm.uiState.value.userEmail).isEmpty()
        assertThat(vm.uiState.value.userRol).isEmpty()
    }

    @Test
    fun `darkModeEnabled is null when not set`() = runTest {
        coEvery { settingsDataStore.isDarkMode } returns flowOf(null)
        val vm = SettingsViewModel(settingsDataStore, workspaceDataStore, authDataStore, logoutUseCase)

        assertThat(vm.uiState.value.darkModeEnabled).isNull()
    }
}
