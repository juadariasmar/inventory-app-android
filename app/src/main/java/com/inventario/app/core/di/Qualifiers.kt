package com.inventario.app.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl
