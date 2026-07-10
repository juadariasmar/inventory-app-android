package com.inventario.app.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun AccessibleIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = description
            role = Role.Button
        },
        enabled = enabled
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
    }
}
