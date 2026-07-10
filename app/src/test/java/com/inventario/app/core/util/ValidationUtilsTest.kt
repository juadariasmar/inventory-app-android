package com.inventario.app.core.util

import android.util.Patterns
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.regex.Matcher

class ValidationUtilsTest {

    @Before
    fun setup() {
        mockkStatic(Patterns::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(Patterns::class)
    }

    // --- validateRequired ---

    @Test
    fun `validateRequired returns error for blank string`() {
        val result = ValidationUtils.validateRequired("", "Nombre")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Nombre es requerido")
    }

    @Test
    fun `validateRequired returns error for whitespace-only string`() {
        val result = ValidationUtils.validateRequired("   ", "Nombre")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Nombre es requerido")
    }

    @Test
    fun `validateRequired returns null for valid string`() {
        assertThat(ValidationUtils.validateRequired("test", "Nombre")).isNull()
    }

    @Test
    fun `validateRequired returns null for string with leading/trailing spaces`() {
        assertThat(ValidationUtils.validateRequired("  test  ", "Nombre")).isNull()
    }

    @Test
    fun `validateRequired uses correct field name in error`() {
        val result = ValidationUtils.validateRequired("", "Precio")
        assertThat(result).isEqualTo("Precio es requerido")
    }

    // --- validatePositive ---

    @Test
    fun `validatePositive returns error for negative number`() {
        val result = ValidationUtils.validatePositive("-5", "Precio")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Precio debe ser mayor a 0")
    }

    @Test
    fun `validatePositive returns error for zero`() {
        val result = ValidationUtils.validatePositive("0", "Precio")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Precio debe ser mayor a 0")
    }

    @Test
    fun `validatePositive returns null for positive number`() {
        assertThat(ValidationUtils.validatePositive("100", "Precio")).isNull()
    }

    @Test
    fun `validatePositive returns error for non-numeric string`() {
        val result = ValidationUtils.validatePositive("abc", "Precio")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Precio debe ser un numero")
    }

    @Test
    fun `validatePositive returns null for decimal positive number`() {
        assertThat(ValidationUtils.validatePositive("19.99", "Precio")).isNull()
    }

    @Test
    fun `validatePositive handles negative decimal`() {
        val result = ValidationUtils.validatePositive("-19.99", "Precio")
        assertThat(result).isEqualTo("Precio debe ser mayor a 0")
    }

    // --- validatePositiveInt ---

    @Test
    fun `validatePositiveInt returns error for non-integer`() {
        val result = ValidationUtils.validatePositiveInt("abc", "Cantidad")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Cantidad debe ser un numero entero")
    }

    @Test
    fun `validatePositiveInt returns error for decimal string`() {
        val result = ValidationUtils.validatePositiveInt("19.99", "Cantidad")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Cantidad debe ser un numero entero")
    }

    @Test
    fun `validatePositiveInt returns error for zero`() {
        val result = ValidationUtils.validatePositiveInt("0", "Cantidad")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Cantidad debe ser mayor a 0")
    }

    @Test
    fun `validatePositiveInt returns error for negative integer`() {
        val result = ValidationUtils.validatePositiveInt("-1", "Cantidad")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Cantidad debe ser mayor a 0")
    }

    @Test
    fun `validatePositiveInt returns null for valid positive integer`() {
        assertThat(ValidationUtils.validatePositiveInt("5", "Cantidad")).isNull()
    }

    // --- validateEmail ---

    @Test
    fun `validateEmail returns null for blank email`() {
        assertThat(ValidationUtils.validateEmail("")).isNull()
    }

    @Test
    fun `validateEmail returns null for blank whitespace email`() {
        assertThat(ValidationUtils.validateEmail("  ")).isNull()
    }

    @Test
    fun `validateEmail returns error for invalid email`() {
        val mockMatcher = mockk<Matcher>()
        every { Patterns.EMAIL_ADDRESS.matcher("invalidemail") } returns mockMatcher
        every { mockMatcher.matches() } returns false

        val result = ValidationUtils.validateEmail("invalidemail")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Email invalido")
    }

    @Test
    fun `validateEmail returns null for valid email`() {
        val mockMatcher = mockk<Matcher>()
        every { Patterns.EMAIL_ADDRESS.matcher("user@example.com") } returns mockMatcher
        every { mockMatcher.matches() } returns true

        val result = ValidationUtils.validateEmail("user@example.com")
        assertThat(result).isNull()
    }

    @Test
    fun `validateEmail returns error for email without at sign`() {
        val mockMatcher = mockk<Matcher>()
        every { Patterns.EMAIL_ADDRESS.matcher("userexample.com") } returns mockMatcher
        every { mockMatcher.matches() } returns false

        val result = ValidationUtils.validateEmail("userexample.com")
        assertThat(result).isEqualTo("Email invalido")
    }

    @Test
    fun `validateEmail returns null for valid email with subdomain`() {
        val mockMatcher = mockk<Matcher>()
        every { Patterns.EMAIL_ADDRESS.matcher("user@mail.example.com") } returns mockMatcher
        every { mockMatcher.matches() } returns true

        assertThat(ValidationUtils.validateEmail("user@mail.example.com")).isNull()
    }
}
