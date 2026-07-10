package com.inventario.app.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun `formatCOP formats zero correctly`() {
        val result = CurrencyUtils.formatCOP(0.0)
        assertThat(result).contains("0")
    }

    @Test
    fun `formatCOP formats integer amounts correctly`() {
        val result = CurrencyUtils.formatCOP(25000.0)
        assertThat(result).contains("25")
        assertThat(result).contains("000")
    }

    @Test
    fun `formatCOP formats small amounts correctly`() {
        val result = CurrencyUtils.formatCOP(1000.0)
        assertThat(result).contains("1")
        assertThat(result).contains("000")
    }

    @Test
    fun `formatCOP formats large amounts correctly`() {
        val result = CurrencyUtils.formatCOP(12500000.0)
        assertThat(result).contains("12")
        assertThat(result).contains("500")
        assertThat(result).contains("000")
    }

    @Test
    fun `formatCOP does not include decimal places`() {
        val result = CurrencyUtils.formatCOP(1500.50)
        assertThat(result).doesNotContain(".5")
    }

    @Test
    fun `formatCOP includes currency symbol`() {
        val result = CurrencyUtils.formatCOP(10000.0)
        assertThat(result).startsWith("$")
    }

    @Test
    fun `formatCOP handles single digit amounts`() {
        val result = CurrencyUtils.formatCOP(5.0)
        assertThat(result).contains("5")
    }

    @Test
    fun `formatCOP handles very large amounts`() {
        val result = CurrencyUtils.formatCOP(999999999.0)
        assertThat(result).startsWith("$")
        assertThat(result).contains("999")
    }

    @Test
    fun `formatCOP handles amount of one`() {
        val result = CurrencyUtils.formatCOP(1.0)
        assertThat(result).contains("1")
    }
}
