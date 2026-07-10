package com.inventario.app.domain.usecase.auth

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    companion object {
        const val WEB_CLIENT_ID = "555660820876-gnp6k4hr1o6vhpimt7rplgkqm8tuqafd.apps.googleusercontent.com"
    }

    suspend operator fun invoke(activity: Activity): Result<Sesion> = runCatching {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(activity, request)
        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
        val idToken = credential.idToken

        val response = authRepository.signInWithGoogle(idToken)
        response.getOrThrow()
    }.recoverCatching { throwable ->
        when {
            throwable is DomainError -> throw throwable
            throwable.message?.contains("CANCELED") == true ->
                throw DomainError.Validation("google", "Inicio de sesion cancelado")
            else -> throw DomainError.NetworkError
        }
    }
}
