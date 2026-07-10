package com.inventario.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.inventario.app.ui.theme.Error
import com.inventario.app.ui.theme.Success
import com.inventario.app.ui.theme.Warning

@Composable
fun StockBadge(
    stock: Int,
    stockMinimo: Int,
    modifier: Modifier = Modifier
) {
    val color: Color
    val icon: androidx.compose.ui.graphics.vector.ImageVector
    val contentText: String

    when {
        stock < stockMinimo -> {
            color = Error
            icon = Icons.Filled.Error
            contentText = "Stock bajo: $stock unidades"
        }
        stock == stockMinimo -> {
            color = Warning
            icon = Icons.Filled.Warning
            contentText = "Stock mínimo: $stock unidades"
        }
        else -> {
            color = Success
            icon = Icons.Filled.CheckCircle
            contentText = "Stock OK: $stock unidades"
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .semantics { contentDescription = contentText },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = stock.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}
