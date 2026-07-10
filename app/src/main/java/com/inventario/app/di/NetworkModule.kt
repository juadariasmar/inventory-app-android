package com.inventario.app.di

import com.inventario.app.data.remote.KtorClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        ktorClientFactory: KtorClientFactory
    ): HttpClient {
        return ktorClientFactory.create()
    }
}
