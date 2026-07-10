package com.inventario.app.di

import android.content.Context
import com.inventario.app.InventarioApp
import com.inventario.app.data.remote.interceptor.AuthInterceptor
import com.inventario.app.data.remote.KtorClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        authInterceptor: AuthInterceptor,
        @ApplicationContext context: Context
    ): HttpClient {
        return KtorClientFactory(authInterceptor).create()
    }
}
