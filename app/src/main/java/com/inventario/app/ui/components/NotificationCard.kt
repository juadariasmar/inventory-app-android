package com.inventario.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inventario.app.core.util.DateUtils
import com.inventario.app.domain.model.Notificacion

@Composable
fun NotificationCard(
    notificacion: Notificacion,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Surface(
                shape = CircleShape,
                color = when {
                    notificacion.tipo.startsWith("STOCK") -> MaterialTheme.colorScheme.errorContainer
                    notificacion.tipo == "INVITACION_ACEPTADA" -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.secondaryContainer
                },
                modifier = Modifier.size(40.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when {
                            notificacion.tipo.contains("STOCK") -> Icons.Filled.Warning
                            notificacion.tipo == "INVITACION_ACEPTADA" -> Icons.Filled.PersonAdd
                            else -> Icons.Filled.Notifications
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(notificacion.titulo, style = MaterialTheme.typography.titleSmall, fontWeight = if (!notificacion.leida) FontWeight.Bold else FontWeight.Normal)
                notificacion.mensaje?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(DateUtils.formatTime(notificacion.creadoEn), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (!notificacion.leida) {
                Box(
                    modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
