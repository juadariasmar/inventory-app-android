package com.inventario.app.di

import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.remote.api.AuthApi
import com.inventario.app.data.remote.api.CategoriaApi
import com.inventario.app.data.remote.api.ClienteApi
import com.inventario.app.data.remote.api.DashboardApi
import com.inventario.app.data.remote.api.MovimientoApi
import com.inventario.app.data.remote.api.OrganizationApi
import com.inventario.app.data.remote.api.ProductoApi
import com.inventario.app.data.remote.api.VentaApi
import com.inventario.app.data.remote.api.WorkspaceApi
import com.inventario.app.data.remote.interceptor.AuthInterceptor
import com.inventario.app.data.repository.AuthRepositoryImpl
import com.inventario.app.data.repository.CategoriaRepositoryImpl
import com.inventario.app.data.repository.ClienteRepositoryImpl
import com.inventario.app.data.repository.DashboardRepositoryImpl
import com.inventario.app.data.repository.MovimientoRepositoryImpl
import com.inventario.app.data.repository.OrganizationRepositoryImpl
import com.inventario.app.data.repository.ProductoRepositoryImpl
import com.inventario.app.data.repository.VentaRepositoryImpl
import com.inventario.app.data.repository.WorkspaceRepositoryImpl
import com.inventario.app.domain.repository.AuthRepository
import com.inventario.app.domain.repository.CategoriaRepository
import com.inventario.app.domain.repository.ClienteRepository
import com.inventario.app.domain.repository.DashboardRepository
import com.inventario.app.domain.repository.MovimientoRepository
import com.inventario.app.domain.repository.OrganizationRepository
import com.inventario.app.domain.repository.ProductoRepository
import com.inventario.app.domain.repository.VentaRepository
import com.inventario.app.domain.repository.WorkspaceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOrganizationRepository(impl: OrganizationRepositoryImpl): OrganizationRepository

    @Binds
    @Singleton
    abstract fun bindWorkspaceRepository(impl: WorkspaceRepositoryImpl): WorkspaceRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(impl: ProductoRepositoryImpl): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindCategoriaRepository(impl: CategoriaRepositoryImpl): CategoriaRepository

    @Binds
    @Singleton
    abstract fun bindMovimientoRepository(impl: MovimientoRepositoryImpl): MovimientoRepository

    @Binds
    @Singleton
    abstract fun bindVentaRepository(impl: VentaRepositoryImpl): VentaRepository

    @Binds
    @Singleton
    abstract fun bindClienteRepository(impl: ClienteRepositoryImpl): ClienteRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(client: HttpClient): AuthApi = AuthApi(client)

    @Provides
    @Singleton
    fun provideOrganizationApi(client: HttpClient): OrganizationApi = OrganizationApi(client)

    @Provides
    @Singleton
    fun provideWorkspaceApi(client: HttpClient): WorkspaceApi = WorkspaceApi(client)

    @Provides
    @Singleton
    fun provideProductoApi(client: HttpClient): ProductoApi = ProductoApi(client)

    @Provides
    @Singleton
    fun provideCategoriaApi(client: HttpClient): CategoriaApi = CategoriaApi(client)

    @Provides
    @Singleton
    fun provideMovimientoApi(client: HttpClient): MovimientoApi = MovimientoApi(client)

    @Provides
    @Singleton
    fun provideVentaApi(client: HttpClient): VentaApi = VentaApi(client)

    @Provides
    @Singleton
    fun provideClienteApi(client: HttpClient): ClienteApi = ClienteApi(client)

    @Provides
    @Singleton
    fun provideDashboardApi(client: HttpClient): DashboardApi = DashboardApi(client)
}
