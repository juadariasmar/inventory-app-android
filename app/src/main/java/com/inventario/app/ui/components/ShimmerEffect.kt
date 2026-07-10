package com.inventario.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    height: Dp = 16.dp,
    cornerRadius: Dp = 4.dp
) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.surfaceVariant,
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

@Composable
fun ShimmerProductCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            ShimmerEffect(width = 150.dp, height = 20.dp)
            Spacer(Modifier.height(8.dp))
            ShimmerEffect(width = 100.dp, height = 14.dp)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                ShimmerEffect(width = 80.dp, height = 14.dp)
                Spacer(Modifier.weight(1f))
                ShimmerEffect(width = 80.dp, height = 14.dp)
            }
        }
    }
}

@Composable
fun ShimmerMetricCard() {
    Card(
        modifier = Modifier
            .aspectRatio(1.2f)
            .padding(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            ShimmerEffect(width = 32.dp, height = 32.dp, cornerRadius = 16.dp)
            Spacer(Modifier.height(8.dp))
            ShimmerEffect(width = 80.dp, height = 24.dp)
            Spacer(Modifier.height(4.dp))
            ShimmerEffect(width = 100.dp, height = 14.dp)
        }
    }
}

@Composable
fun ShimmerListItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(width = 40.dp, height = 40.dp, cornerRadius = 20.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            ShimmerEffect(width = 120.dp, height = 16.dp)
            Spacer(Modifier.height(4.dp))
            ShimmerEffect(width = 180.dp, height = 12.dp)
        }
    }
}

@Composable
fun ShimmerProductList() {
    Column {
        repeat(6) {
            ShimmerProductCard()
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun ShimmerDashboardGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ShimmerMetricCard()
        ShimmerMetricCard()
    }
}

@Composable
fun ShimmerMovementList() {
    Column {
        repeat(5) {
            ShimmerListItem()
        }
    }
}
