package com.inventario.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorClientFactory @Inject constructor() {

    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    cookieJar(object : CookieJar {
                        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                            cookieStore[url.host] = cookies
                        }

                        override fun loadForRequest(url: HttpUrl): List<Cookie> {
                            return cookieStore[url.host] ?: emptyList()
                        }
                    })
                }
            }

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
            }
        }
    }
}
