package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.OnboardingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun completeOnboarding(): OnboardingResponse {
        return client.patch("usuarios/onboarding").body()
    }
}
