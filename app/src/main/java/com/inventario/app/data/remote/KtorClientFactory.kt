package com.inventario.app.data.remote

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
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorClientFactory @Inject constructor() {

    @Volatile
    var authToken: String? = null

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
                val token = authToken
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}
