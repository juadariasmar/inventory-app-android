package com.inventario.app.core.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("es", "CO"))
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale("es", "CO"))
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))

    fun formatDisplay(isoDate: String): String {
        return try {
            val date = java.time.Instant.parse(isoDate)
                .atZone(java.time.ZoneId.of("America/Bogota"))
                .toLocalDateTime()
            displayFormat.format(java.util.Date.from(date.atZone(java.time.ZoneId.systemDefault()).toInstant()))
        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatTime(isoDate: String): String {
        return try {
            val date = java.time.Instant.parse(isoDate)
                .atZone(java.time.ZoneId.of("America/Bogota"))
                .toLocalDateTime()
            timeFormat.format(java.util.Date.from(date.atZone(java.time.ZoneId.systemDefault()).toInstant()))
        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatDate(isoDate: String): String {
        return try {
            val date = java.time.Instant.parse(isoDate)
                .atZone(java.time.ZoneId.of("America/Bogota"))
                .toLocalDateTime()
            dateFormat.format(java.util.Date.from(date.atZone(java.time.ZoneId.systemDefault()).toInstant()))
        } catch (e: Exception) {
            isoDate
        }
    }
}
