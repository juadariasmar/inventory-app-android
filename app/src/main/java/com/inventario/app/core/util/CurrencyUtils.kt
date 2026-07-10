package com.inventario.app.core.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val copFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatCOP(amount: Double): String {
        return copFormat.format(amount)
    }
}
