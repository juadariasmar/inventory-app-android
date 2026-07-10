package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.SignInRequest
import com.inventario.app.data.remote.dto.SignInResponse
import com.inventario.app.data.remote.dto.SessionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun signIn(email: String, password: String): SignInResponse {
        return client.post("auth/sign-in/email") {
            contentType(ContentType.Application.Json)
            setBody(SignInRequest(email, password))
        }.body()
    }

    suspend fun getSession(token: String): SessionResponse {
        return client.get("auth/get-session") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun signOut(token: String) {
        client.post("auth/sign-out") {
            header("Authorization", "Bearer $token")
        }
    }
}
