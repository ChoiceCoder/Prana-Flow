package com.pranaflow.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranaflow.ui.theme.PranaTheme

@Composable
fun PranaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_glow_alpha"
    )

    val colors = PranaTheme.colors

    Box(
        modifier = modifier
            .width(240.dp)
            .height(56.dp)
            .bounceClick(onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = CornerRadius(28.dp.toPx())

            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colors.gold.copy(alpha = glowAlpha * 0.6f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.maxDimension * 0.8f
                ),
                cornerRadius = cornerRadius,
                size = Size(size.width + 16.dp.toPx(), size.height + 16.dp.toPx()),
                topLeft = Offset(-8.dp.toPx(), -8.dp.toPx())
            )

            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.goldBright.copy(alpha = 0.55f),
                        colors.gold.copy(alpha = 0.40f)
                    )
                ),
                cornerRadius = cornerRadius
            )

            drawRoundRect(
                color = colors.gold.copy(alpha = 0.7f + glowAlpha * 0.3f),
                cornerRadius = cornerRadius,
                style = Stroke(width = 1.2.dp.toPx())
            )
        }

        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleSmall.copy(
                color = colors.goldPale,
                letterSpacing = 3.sp
            )
        )
    }
}
