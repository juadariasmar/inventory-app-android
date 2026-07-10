package com.inventario.app.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `formatDisplay formats ISO date correctly`() {
        val result = DateUtils.formatDisplay("2024-01-15T10:30:00Z")
        assertThat(result).contains("2024")
    }

    @Test
    fun `formatDisplay returns original string on parse error`() {
        val result = DateUtils.formatDisplay("invalid-date")
        assertThat(result).isEqualTo("invalid-date")
    }

    @Test
    fun `formatDisplay handles empty string`() {
        val result = DateUtils.formatDisplay("")
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `formatTime extracts time`() {
        val result = DateUtils.formatTime("2024-01-15T10:30:00Z")
        assertThat(result).contains("10")
    }

    @Test
    fun `formatTime returns original string on parse error`() {
        val result = DateUtils.formatTime("bad-input")
        assertThat(result).isEqualTo("bad-input")
    }

    @Test
    fun `formatTime handles empty string`() {
        val result = DateUtils.formatTime("")
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `formatDate formats ISO date correctly`() {
        val result = DateUtils.formatDate("2024-01-15T10:30:00Z")
        assertThat(result).contains("2024")
    }

    @Test
    fun `formatDate returns original string on parse error`() {
        val result = DateUtils.formatDate("not-a-date")
        assertThat(result).isEqualTo("not-a-date")
    }

    @Test
    fun `formatDate handles empty string`() {
        val result = DateUtils.formatDate("")
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `formatDisplay handles date with timezone offset`() {
        val result = DateUtils.formatDisplay("2024-06-15T14:30:00-05:00")
        assertThat(result).contains("2024")
    }

    @Test
    fun `formatTime handles timezone offset`() {
        val result = DateUtils.formatTime("2024-06-15T14:30:00-05:00")
        assertThat(result).isNotEmpty()
    }
}
