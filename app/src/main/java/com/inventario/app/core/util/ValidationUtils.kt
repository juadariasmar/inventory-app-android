package com.inventario.app.core.util

object ValidationUtils {
    fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName es requerido" else null
    }

    fun validatePositive(value: String, fieldName: String): String? {
        val number = value.toDoubleOrNull()
        return when {
            number == null -> "$fieldName debe ser un numero"
            number <= 0 -> "$fieldName debe ser mayor a 0"
            else -> null
        }
    }

    fun validatePositiveInt(value: String, fieldName: String): String? {
        val number = value.toIntOrNull()
        return when {
            number == null -> "$fieldName debe ser un numero entero"
            number <= 0 -> "$fieldName debe ser mayor a 0"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email invalido"
        } else {
            null
        }
    }
}
