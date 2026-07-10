package com.inventario.app.data.remote

import com.inventario.app.data.remote.interceptor.AuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class KtorClientFactory(
    private val authInterceptor: AuthInterceptor
) {
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("Ktor", message)
                    }
                }
                level = LogLevel.BODY
            }

            defaultRequest {
                url("https://inventory-project-ten.vercel.app/api/")
                val token = runBlocking { authInterceptor.getToken() }
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}
