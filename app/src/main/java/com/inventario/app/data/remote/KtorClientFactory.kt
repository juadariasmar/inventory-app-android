package com.inventario.app.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

private val Context.cookieStore by preferencesDataStore(name = "cookies")

@Singleton
class KtorClientFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var cookieMap = mutableMapOf<String, MutableList<Cookie>>()

    init {
        runBlocking {
            loadFromPrefs()
        }
    }

    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    cookieJar(object : CookieJar {
                        override fun saveFromResponse(url: HttpUrl, responseCookies: List<Cookie>) {
                            val host = url.host
                            val existing = cookieMap[host] ?: mutableListOf()
                            for (cookie in responseCookies) {
                                val idx = existing.indexOfFirst { it.name == cookie.name }
                                if (idx >= 0) existing[idx] = cookie else existing.add(cookie)
                            }
                            cookieMap[host] = existing
                            runBlocking { saveToPrefs() }
                        }

                        override fun loadForRequest(url: HttpUrl): List<Cookie> {
                            val host = url.host
                            val stored = cookieMap[host] ?: return emptyList()
                            val now = System.currentTimeMillis()
                            val valid = stored.filter { it.expiresAt > now }
                            if (valid.size != stored.size) {
                                cookieMap[host] = valid.toMutableList()
                                runBlocking { saveToPrefs() }
                            }
                            return valid
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

    private suspend fun saveToPrefs() {
        val serialized = cookieMap.entries.joinToString("\n") { (host, cookies) ->
            cookies.joinToString("\n") { "${host}|${it.name}|${it.value}|${it.domain}|${it.path}|${it.expiresAt}" }
        }
        context.cookieStore.edit { prefs ->
            prefs[stringPreferencesKey("data")] = serialized
        }
    }

    private suspend fun loadFromPrefs() {
        val raw = context.cookieStore.data.first()[stringPreferencesKey("data")]
        cookieMap.clear()
        raw?.lines()?.forEach { line ->
            if (line.isBlank()) return@forEach
            val parts = line.split("|")
            if (parts.size < 6) return@forEach
            try {
                val cookie = Cookie.Builder()
                    .name(parts[1]).value(parts[2])
                    .domain(parts[3]).path(parts[4])
                    .expiresAt(parts[5].toLongOrNull() ?: Long.MAX_VALUE)
                    .build()
                val list = cookieMap[parts[0]] ?: mutableListOf()
                list.add(cookie)
                cookieMap[parts[0]] = list
            } catch (_: Exception) { }
        }
    }
}
